package edu.eci.arep.Microservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.eci.arep.Microservice.dto.UserDTO;
import edu.eci.arep.Microservice.exception.UserNotFoundException;
import edu.eci.arep.Microservice.model.User;
import edu.eci.arep.Microservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class UserService {

    private UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    // User

    public List<User> getAllUsers(){
        return new ArrayList<>(userRepository.findAll());
    }

    public User getUserById(String id) throws Exception{
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) return user.get();
        throw new Exception(id);
    }

    public User saveUser(UserDTO userDTO){
        User newUser = new User(userDTO);
        userRepository.save(newUser);
        return newUser;
    }

    public boolean auth(UserDTO userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            return false;
        }
        return BCrypt.checkpw(userDto.getPassword(), user.getPassword());
    }

    public User updateUser(String id, UserDTO userDTO) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()) throw new Exception(id);
        User updateUser = user.get();
        updateUser.update(userDTO);
        userRepository.save(updateUser);
        return updateUser;
    }

    public void deleteUser(String id) throws UserNotFoundException {
        if(!userRepository.existsById(id)) throw new UserNotFoundException(id);
        userRepository.deleteById(id);
    }

    /*    // Design

    // GET Requests

    public List<Design> getAllPublicDesigns() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), new TypeReference<List<Design>>() {});
    }

    public Design getDesignById(String idDesign) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs/" + idDesign))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), Design.class);
    }

    public List<Design> getDesignsOfUser(String idUser) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs/users/" + idUser))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), new TypeReference<List<Design>>() {});
    }

    public List<Quotation> getAllQuotations(String idDesign) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs/" + idDesign + "/quotations"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), new TypeReference<List<Quotation>>() {});
    }

    public Quotation getQuotationById(String quotationId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs/quotations/" + quotationId))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), Quotation.class);
    }

    // POST Requests

    public Design addDesign(DesignDTO design) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = objectMapper.writeValueAsString(design);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
        return objectMapper.readValue(response.body(), Design.class);
    }

    public Quotation createQuotation(String idQuotation, QuotationDTO quotationDTO) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = objectMapper.writeValueAsString(quotationDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs/" + idQuotation + "/quotations"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
        return objectMapper.readValue(response.body(), Quotation.class);
    }

    // PUT Requests

    public Design updateDesign(String idDesign, DesignDTO designDTO) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = objectMapper.writeValueAsString(designDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs/" + idDesign))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
        return objectMapper.readValue(response.body(), Design.class);
    }

    public Quotation updateQuotation(String quotationId, QuotationDTO quotationDTO) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = objectMapper.writeValueAsString(quotationDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs/quotations/" + quotationId))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
        return objectMapper.readValue(response.body(), Quotation.class);
    }

    // DELETE Requests

    public void deleteDesign(String idDesign) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs/" + idDesign))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
    }

    public void deleteQuotation(String idQuotation) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.5:8080/v1/designs/quotations" + idQuotation))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
    }

    // Product

    // GET Requests

    public List<Product> getAllProducts() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.2:8080/v1/products"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), new TypeReference<List<Product>>() {});
    }

    public Product getProductById(String id) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.2:8080/v1/products/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), Product.class);
    }

    public Double getProductPrice(String productId) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.2:8080/v1/products/price/" + productId))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), new TypeReference<Double>() {});
    }

    public String getProductDimensions(String productId) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.2:8080/v1/products/dimensions/" + productId))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), new TypeReference<String>() {});
    }

    public String getProductStore(String productId) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.2:8080/v1/products/seller/" + productId))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), new TypeReference<String>() {});
    }

    public List<Product> sortProducts(String criteria, String category) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.2:8080/v1/products/sort/" + criteria + "/" + category))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), new TypeReference<List<Product>>() {});
    }

    // POST Requests

    public Product createProduct(ProductDTO productDTO) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = objectMapper.writeValueAsString(productDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.2:8080/v1/products"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() >= 300){
            throw new Exception();
        }
        return objectMapper.readValue(response.body(), Product.class);
    }

    // PUT Requests

    public Product updateProduct(String idProduct, ProductDTO productDTO) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = objectMapper.writeValueAsString(productDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.2:8080/v1/products/" + idProduct))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
        return objectMapper.readValue(response.body(), Product.class);
    }

    // DELETE Requests

    public void deleteProduct(String idProduct) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.2:8080/v1/products/" + idProduct))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
    }

    // Purchase

    // GET Requests

    public List<Purchase> getAllPurchases() throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.3:8080/v1/purchases"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), new TypeReference<List<Purchase>>() {});
    }

    public Purchase getPurchaseById(String id) throws Exception{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.3:8080/v1/purchases/" + id))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.body(), Purchase.class);
    }

    // POST Requests

    public Purchase createPurchase(PurchaseDTO purchaseDTO) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = objectMapper.writeValueAsString(purchaseDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.3:8080/v1/purchases"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() >= 300){
            throw new Exception();
        }
        return objectMapper.readValue(response.body(), Purchase.class);
    }

    // DELETE Requests

    public void deletePurchase(String idPurchase) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://10.0.0.3:8080/v1/purchases/" + idPurchase))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new Exception();
        }
    }*/

}
