package cybersignclient.hmac;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    private String MaGD;
    private String CTSNguoidung;
    private String CTSRootCA;
    private String CTSSubCA;
    private String APPID;
    private String Secret;
    private String FileAnhCK;
    private float TrangThai;
    private String MoTa;
    private String ExpiredDate;
}
