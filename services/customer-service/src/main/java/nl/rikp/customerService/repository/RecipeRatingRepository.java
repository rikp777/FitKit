package nl.rikp.customerService.repository;

import nl.rikp.customerService.model.RecipeRating;
import nl.rikp.customerService.model.RecipeRatingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, Long> {

    @Query("""
            SELECT r
            FROM RecipeRating r
            WHERE r.customer.id = :customerId
            """)
    Page<RecipeRating> findAllPaginated(
            @Param("customerId") Long customerId,
            Pageable pageable
    );

    @Query("""
            SELECT r
            FROM RecipeRating r
            WHERE r.customer.id = :customerId
            AND r.recipeId = :recipeId
            """)
    Optional<RecipeRating> findById(
            @Param("customerId") Long customerId,
            @Param("recipeId") Long recipeId
    );


    @Query("""
        SELECT COUNT(r)
        FROM RecipeRating r
        WHERE r.customer.id = :customerId
        AND r.createdDate = CURRENT_DATE
        """)
    Integer countRatingsCreatedTodayByCustomer(
            @Param("customerId") Long customerId
    );

    @Query("""
        SELECT SUM(r.updatedCount)
        FROM RecipeRating r
        WHERE r.customer.id = :customerId
        AND r.updatedDate = CURRENT_DATE
        """)
    Integer countRatingsUpdatedTodayByCustomer(
            @Param("customerId") Long customerId
    );

    @Query("""
            SELECT COUNT(r)
            FROM RecipeRating r
            WHERE r.customer.id = :customerId
            """)
    Integer countTotalRatingsByCustomer(
            @Param("customerId") Long customerId
    );

    @Query("""
            SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
            FROM RecipeRating r
            WHERE r.customer.id = :customerId
            AND r.recipeId = :recipeId
            AND r.rating = :recipeRatingType
            """)
    boolean existsByIdAndRecipeRatingType(
            @Param("customerId") Long customerId,
            @Param("recipeId") Long recipeId,
            @Param("recipeRatingType") RecipeRatingType recipeRatingType
    );
}
