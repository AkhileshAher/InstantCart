package in.akhilesh.instantcart.utils;

public class OtpGenerator {

    public static Integer getOtp() {
        int otp = (int) (Math.random() * 900000) + 100000;
        // SEND TO USER
        System.out.println("Sending OTP TO USER");
        return otp;
    }


}
