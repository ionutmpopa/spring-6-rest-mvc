package guru.springframework.spring6restmvc.controller.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
public class CustomerDTO {

    private UUID customerId;
    private String customerName;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
