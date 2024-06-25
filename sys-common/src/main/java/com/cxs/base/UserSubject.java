package com.cxs.base;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

  
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubject {
    private String id;
    private String username;
    private List<String> authentications;
}
