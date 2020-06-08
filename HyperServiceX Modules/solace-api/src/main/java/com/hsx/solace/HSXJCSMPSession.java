package com.hsx.solace;

import com.solacesystems.jcsmp.JCSMPSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HSXJCSMPSession {

    private JCSMPSession session;
    private int siteNo;

}
