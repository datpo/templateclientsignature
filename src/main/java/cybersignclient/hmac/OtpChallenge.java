package cybersignclient.hmac;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Setter
@Getter
public class OtpChallenge {
    private String transactionid;
    private String challenge;
}