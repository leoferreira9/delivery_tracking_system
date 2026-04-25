package com.leonardo.delivery_tracking_system.specification;

import com.leonardo.delivery_tracking_system.enums.DeliveryStatus;
import com.leonardo.delivery_tracking_system.model.Delivery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DeliverySpecifications {
    public static Specification<Delivery> hasStatus(DeliveryStatus status){
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Delivery> hasEstablishmentId(Long id){
        return (root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("establishment").get("id"), id);
    }

    public static Specification<Delivery> isBetween(LocalDate startDate, LocalDate endDate){
        return (root, query, criteriaBuilder) -> {
            if(startDate == null && endDate == null) return criteriaBuilder.conjunction();

            LocalDateTime start = null;
            LocalDateTime end = null;

            if(startDate != null){
                start = startDate.atStartOfDay();
            }

            if(endDate != null){
                end = endDate.atTime(23, 59, 59);
            }

            if(start != null && end == null){
                return criteriaBuilder.greaterThanOrEqualTo(
                        root.get("createdAt"),
                        start
                );
            }

            if(start == null && end != null){
                return criteriaBuilder.lessThanOrEqualTo(
                        root.get("createdAt"),
                        end
                );
            }

            return criteriaBuilder.between(root.get("createdAt"), start, end);
        };
    }


}
