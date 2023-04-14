package guru.springframework.spring6restmvc.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    private UUID customerId;

    @Version
    private String customerName;
    private Integer version;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
