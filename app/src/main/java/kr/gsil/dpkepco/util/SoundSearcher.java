package kr.gsil.dpkepco.util;

public class SoundSearcher {
	
private static final char HANGUL_BEGIN_UNICODE = 44032; // �? 
private static final char HANGUL_LAST_UNICODE = 55203; // ?��
private static final char HANGUL_BASE_UNIT = 588;//각자?�� 마다 �?�??�� �??��?��
//?��?��
private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' }; 


/**
 * ?��?�� 문자�? INITIAL_SOUND?���? �??��.
 * @param searchar
 * @return
 */
private static boolean isInitialSound(char searchar){ 
	for(char c:INITIAL_SOUND) { 
		if(c == searchar){ 
			return true; 
		} 
	} 
	return false; 
} 

/**
 * ?��?�� 문자?�� ?��?��?�� ?��?��?��.
 *  
 * @param c �??��?�� 문자
 * @return
 */
private static char getInitialSound(char c) { 
	int hanBegin = (c - HANGUL_BEGIN_UNICODE); 
	int index = hanBegin / HANGUL_BASE_UNIT; 
	return INITIAL_SOUND[index]; 
} 

/**
 * ?��?�� 문자�? ?���??���? �??��
 * @param c 문자 ?��?��
 * @return
 */
private static boolean isHangul(char c) { 
	return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE; 
} 

/**
 * ?��?��?��.
 */
public SoundSearcher() { } 

	/** * �??��?�� ?��?��. 초성 �??�� ?���? �??��?��. 
	 * @param value : �??�� ???�� ex> 초성�??��?��?��?�� 
	 * @param search : �??��?�� ex> ?���??��?��?�� 
	 * @return 매칭 ?��?���? 찾으�? true 못찾?���? false. */ 
	public static boolean matchString(String value, String search){ 
			int t = 0; 
			int seof = value.length() - search.length(); 
			int slen = search.length(); 
		
			if(seof < 0) 
			return false; //�??��?���? ?�� 길면 false�? 리턴?��?��. 
		
			for(int i = 0;i <= seof;i++){ 
				t = 0; 
				while(t < slen){ 
					if(isInitialSound(search.charAt(t))==true && isHangul(value.charAt(i+t))) { 
						//만약 ?��?�� char?�� 초성?���? value�? ?���??���?
						if(getInitialSound(value.charAt(i+t))==search.charAt(t)) 
						//각각?�� 초성?���? 같�?�? 비교?��?��
							t++; 
						else 
							break; 
					} else { 
					//char?�� 초성?�� ?��?��?���?
						if(value.charAt(i+t)==search.charAt(t)) 
						//그냥 같�?�? 비교?��?��. 
							t++; 
						else 
							break; 
					} 
				} 
				if(t == slen) 
					return true; //모두 ?��치한 결과�? 찾으�? true�? 리턴?��?��. 
			} 
		return false; //?��치하?�� 것을 찾�? 못했?���? false�? 리턴?��?��.
	}
}
