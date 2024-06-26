package nl.rikp.customerService.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nl.rikp.customerService.dto.CustomerRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the customer service system.
 */
 //Lombok
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter

//JPA
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "premium_level")
    private PremiumLevel premiumLevel;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Settings settings;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FoodItem> foodItems = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<FoodAllergy> foodAllergies = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecipeRating> ratings = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    @NotNull(message = "Language cannot be null")
    private Language language;

    public void update(CustomerRequest request){
        this.firstName = request.firstName();
        this.lastName = request.lastName();
        this.email = request.email();
        this.phone = request.phone();
    }

    public void addFoodItem(FoodItem foodItem){
        this.foodItems.add(foodItem);
    }

    public void removeFoodItem(FoodItem foodItem){
        foodItem.update(FoodItemPriorityLevel.ONE, FoodItemPreferenceType.NEUTRAL);
        //replace the old food item with the updated one
        this.foodItems.set(this.foodItems.indexOf(foodItem), foodItem);
    }

    public List<FoodItem> getFoodItemPreferred(){
        if(this.foodItems.isEmpty())
            return List.of();
        return this.foodItems.stream()
                .filter(foodItem -> foodItem.getPreferenceType() == FoodItemPreferenceType.PREFERRED)
                .toList();
    }

    public List<FoodItem> getFoodItemDisliked(){
        if(this.foodItems.isEmpty())
            return List.of();
        return this.foodItems.stream()
                .filter(foodItem -> foodItem.getPreferenceType() == FoodItemPreferenceType.DISLIKED)
                .toList();
    }

    public List<FoodItem> getFoodItemNeutral(){
        if(this.foodItems.isEmpty())
            return List.of();
        return this.foodItems.stream()
                .filter(foodItem -> foodItem.getPreferenceType() == FoodItemPreferenceType.NEUTRAL)
                .toList();
    }

    public void addFoodAllergy(FoodAllergy foodAllergy){
        this.foodAllergies.add(foodAllergy);
    }

    public void removeFoodAllergy(FoodAllergy foodAllergy){
        this.foodAllergies.remove(foodAllergy);
    }

    public void addRecipeRating(RecipeRating recipeRating) {
        this.ratings.add(recipeRating);
    }
    public void removeRecipeRating(RecipeRating recipeRating) {
        this.ratings.remove(recipeRating);
    }

    public void addSettings(Settings settings){
        this.settings = settings;
    }
}
