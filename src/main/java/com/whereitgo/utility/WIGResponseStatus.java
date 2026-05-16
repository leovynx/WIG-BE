package com.whereitgo.utility;

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