package cybersignclient.hmac;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@Data
@Builder
@Setter
@Getter

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResp {
    private Object error;
    private Object description;
    private int status;
    private Object obj;
    public ApiResp()
    {
        status = RespCode.OK.value();
        error = RespCode.OK.getReasonPhrase();
    }
    public ApiResp(RespCode err) {
        status = err.value();
        error = err.getReasonPhrase();
    }

    public ApiResp(RespCode err , String msg ) {
        status = err.value();
        error = err.getReasonPhrase() ;
        description = msg;
    }
}

