package cybersignclient.hmac;


public enum RespCode {
    OK(0, "Thành công"),
    DUPLICATE(100, "Trường thông tin đã tồn tại"),
    RESOURCE_NOTFOUND(101, "Không tìm thấy đối tượng"),
    UNKNOW(102, "Lỗi không xác định"),
    CERTIFICATE(1000, "Dữ liệu Certififcate không hợp lệ"),
    CERTIFICATE_DATA(1001, "Dữ liệu Certififcate không hợp lệ"),
    CERTIFICATE_EXPIRED(1002, "Chứng thư số hết hạn"),

    REQUEST_NOT_FOUND(2002, "Không tìm thấy request đã tạo"),
    USERINFO_INVALID(2003, "Thông tin khách hàng tồn tại nhưng không hợp lệ"),
    INPUT_DATA_FORMAT_INVALID(2004, "Dữ liệu kí không hợp lệ"),
    HttpHostConnectException( 2005, "HttpHostConnectException"),
    ResourceAccessException( 2006, "ResourceAccessException"),
    NullPointerException( 2007, "NullPointerException"),
    HttpClientErrorException( 2008, "HttpClientErrorException"),
    InvalidAccessTokenException( 2009, "InvalidAccessTokenException"),
    CREDENTIAL_INVALID(2010, "Xác thực không thành công"),
    PERMISSION_DENY(2011, "Không có quyền truy cập"),
    SERVICE_EXPIRED(2012, "Dịch vụ hết hạn"),
    SMS_OTP_INVALID(7000, "Sai OTP"),
    SMS_INTERACT(7003, "Lỗi giao tiếp sms");



    private final Integer value;
    private final String reasonPhrase;

    private RespCode(Integer value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public String stringvalue() {
        return this.value.toString();
    }

    public Integer value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public static RespCode valueOf(int errorCode) {
        RespCode error = resolve(errorCode);
        if (error == null) {
            throw new IllegalArgumentException("No matching constant for [" + errorCode + "]");
        } else {
            return error;
        }
    }

    public static RespCode resolve(int errorCode) {
        RespCode[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            RespCode error = var1[var3];
            if (error.value == errorCode) {
                return error;
            }
        }

        return null;
    }


}
