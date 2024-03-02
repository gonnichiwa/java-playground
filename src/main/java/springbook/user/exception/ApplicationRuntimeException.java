package springbook.user.exception;

public class ApplicationRuntimeException extends RuntimeException {
    public ApplicationRuntimeException(Throwable cause) {
        super(cause);
        System.out.println(cause.getMessage()); // 로그 저장용
        System.out.println("Duplicate User Id"); // clent message 송신용
    }
}
