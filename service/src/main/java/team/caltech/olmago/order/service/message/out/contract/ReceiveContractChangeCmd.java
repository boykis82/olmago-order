package team.caltech.olmago.order.service.message.out.contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ReceiveContractChangeCmd {
  private long orderId;
  private long customerId;

  private LocalDateTime changeReceivedDateTime;

  private Long packageContractId;
  private String beforePackageProductCode;
  private String afterPackageProductCode;

  private Long optionContractId;
  private String beforeOptionProductCode;
  private String afterOptionProductCode;
  private boolean keepingBeforeContract;
}
