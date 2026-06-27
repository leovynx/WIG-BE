package com.whereitgo.utility.httpEntity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WIGResponseStatus {

    private int statusCode;

    private String statusMessage;
}