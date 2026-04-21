package com.leonardo.delivery_tracking_system.specification;

import com.leonardo.delivery_tracking_system.enums.DeliveryStatus;
import com.leonardo.delivery_tracking_system.model.Delivery;
import org.springframework.data.jpa.domain.Specification;

public class DeliverySpecifications {
    public static Specification<Delivery> hasStatus(DeliveryStatus status){
        return ((root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status));
    }

    public static Specification<Delivery> hasEstablishmentId(Long id){
        return ((root, query, criteriaBuilder) ->
                id == null ? null : criteriaBuilder.equal(root.get("establishment").get("id"), id));
    }
}
