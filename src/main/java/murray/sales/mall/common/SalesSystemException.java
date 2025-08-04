package murray.sales.mall.common;
/*
 * @author Murray
 * @email murray50325487@.gmail.com
 */
public class SalesSystemException extends RuntimeException {

    public SalesSystemException() {
    }

    public SalesSystemException(String message) {
        super(message);
    }

    /**
     * Throw an Exception
     *
     * @param message
     */
    public static void fail(String message) {
        throw new SalesSystemException(message);
    }

}
