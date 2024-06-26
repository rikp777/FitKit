package nl.rikp.customerService.client;

import org.springframework.stereotype.Service;

@Service
public class RecipeClient {

    public boolean existsById(Long recipeId) {
        return true;
    }

}
