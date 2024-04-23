import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class Palindrome {

    @Test
    public void 회문(){
        String palindrome = "소주만병만주소";
        String notPalindrome = "소주만병좀주소";

        assertThat(isPalindrome(palindrome,    0, palindrome.length() - 1), is(true) );
        assertThat(isPalindrome(notPalindrome, 0, palindrome.length() - 1), is(false) );
    }

    private Boolean isPalindrome(String str, int start, int end) {
        if(str.charAt(start) != str.charAt(end)) {
            return false;
        }
        if(end - start <= 1){
            return true;
        }
        return isPalindrome(str, start+1, end-1);
    }
}
