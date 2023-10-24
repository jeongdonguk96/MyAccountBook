package com.accountbook.myaccountbook.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExpenseCategoryEnum {
    GROCERY("식료품"),
    EAT_OUT("외식"),
    DELIVERY("베달"),
    DAILY_NECESSITY("생필품"),
    HOME_APPLIANCES("가전제품"),
    ELECTRONIC_PRODUCTS("전자제품"),
    CLOTHES("의류"),
    MEDICAL("의료"),
    PHONE("통신"),
    TRANSPORTATION("교통"),
    ENTERTAINMENT("여가"),
    PET("반려동물"),
    DEPOSIT("예/적금"),
    INSURANCE("보험"),
    RENT("월세"),
    UTILITIES("공과금"),
    REPAYMENT("상환금"),
    TRIBUTE("경조사"),
    ETC("기타");

    private String value;
}
