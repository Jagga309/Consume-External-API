package com.app.ExAPi.service;

//import ch.qos.logback.classic.Logger;
import com.app.ExAPi.exceptions.PostNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.app.ExAPi.entities.Post;
import com.app.ExAPi.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    String baseUrl = "https://jsonplaceholder.typicode.com/posts";

    public void savePostsFromAPI(){
        RestTemplate restTemplate = new RestTemplate();

        Map<String,Object>[] posts = restTemplate.getForObject(baseUrl,Map[].class);

        for(Map<String,Object> postMap : posts){
            Post post = new Post();
            post.setTitle((String) postMap.get("title"));
            post.setBody((String) postMap.get("body"));
            postRepository.save(post);
        }
    }
    public Post savePostById(Long id){
        System.out.println("running...");
        RestTemplate restTemplate = new RestTemplate();

        StringBuilder stringBuilder = new StringBuilder(baseUrl);

        String url = stringBuilder.append("/"+id).toString();
        System.out.println(url);

        // logging a debugging message..........
        logger.debug("Fetching data from URL : {}",url);

       try{
           Map<String,Object> postMap = restTemplate.getForObject(url,Map.class);
           if(postMap!=null){
               Post post = new Post();
               post.setTitle((String) postMap.get("title"));
               post.setBody((String) postMap.get("body"));

               // Logging an info message....
               logger.info("Saved Post With ID : {}" , id);

               return postRepository.save(post);
           }else{
//               logger.error("Failed to fetch data for ID : {}", id);
               throw new PostNotFoundException("Post Not Found for ID :" + id);
           }
       } catch (RestClientException exception){
           logger.error("Failed to fetch data for ID : {}", id,exception);
           throw new PostNotFoundException("Error Fetching Data from URL : "+ url);
       }
    }
    public List<Post> getPosts() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Post>> response = restTemplate.exchange(baseUrl, HttpMethod.GET,
                null,new ParameterizedTypeReference<List<Post>>() {});
        for(Post post : response.getBody()){
            System.out.println(post);
        }
        return response.getBody();
    }
}
