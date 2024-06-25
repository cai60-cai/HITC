package com.cxs.dto.message;

import lombok.Data;

import java.util.List;
import java.util.Set;

  
@Data
public class ReadMessageDTO {

    private Set<Integer> messageIdList;
}
