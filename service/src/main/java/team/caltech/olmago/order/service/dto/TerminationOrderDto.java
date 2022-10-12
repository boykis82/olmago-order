package team.caltech.olmago.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class TerminationOrderDto {
  private long customerId;
  private Long packageContractId;
  private Long optionContractId;
  private List<Long> unitContractIds;
}
