package team.caltech.olmago.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChangeOrderDto {
  private long customerId;
  
  private Long packageContractId;
  private String beforePackageProductCode;
  private String afterPackageProductCode;
  
  private Long optionContractId;
  private String beforeOptionProductCode;
  private String afterOptionProductCode;
  private boolean keepingBeforeContract;
  
  public boolean isChangingPackageContract() {
    return packageContractId != null && packageContractId != 0 &&
        beforePackageProductCode != null && !beforePackageProductCode.isEmpty() &&
        afterPackageProductCode != null && !afterPackageProductCode.isEmpty();
  }
  
  public boolean isChangingOptionContract() {
    return optionContractId != null && optionContractId != 0 &&
        beforeOptionProductCode != null && !beforeOptionProductCode.isEmpty() &&
        afterOptionProductCode != null && !afterOptionProductCode.isEmpty();
  }
}
