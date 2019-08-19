package shorten.util;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import org.junit.Test;

import com.shorten.util.ShortenUtil;

public class ShortenUtilTest extends ShortenUtil {

	/**
	 * 단축URL 생성 여부
	 */
	@Test
	public void testShortenURL() {
		//fail("Not yet implemented");
		String originUrl = "http://daum.net";
		String ctxUrl = "localhost";
		ShortenUtil u = new ShortenUtil(8, ctxUrl);
		String shortenUrl = u.shortenURL(originUrl);
		
		// 1.단축 URL 생성여부 체크
		try{
			assertNotNull(shortenUrl);
			if(shortenUrl.indexOf(ctxUrl) < 0) {
				fail("1-1.Fail to Shorten URL Ceate!!" + " / shortenUrl : " + shortenUrl);
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("1-2.Fail to Shorten URL Ceate!!");
		}		
				
		// 2.단축 URL ID길이 체크
		try{
			int idLength = (shortenUrl.replace(ctxUrl+"/", "")).length();
			assertEquals(8, idLength);
		} catch(Exception e) {
			e.printStackTrace();
			fail("1-3.Fail to ID length Check!!");
		}
	}

	/**
	 * 동일URL등록시 동일한 단축URL이 리턴되는지 여부
	 */
	@Test
	public void testExistShortenUrl() {
		ShortenUtil u = new ShortenUtil(8, "localhost");
		// 하기와 같이 총 5개의 단축URL을 생성
		String originUrls[] = { "http://www.google.com/",	//1. google : 1개생성
								"http://www.daum.net",   	//2. daum : 1개생성
								"http://www.naver.com",		//3. naver : 1개생성
								"http://www.amazon.com","http://www.amazon.com/page1.php", 		//4. amazon : 2개생성
								};
		// 검증을 위한 배열		
		Vector v = new Vector();
		
		File file = new File(shortenUrlFileDrive,shortenUrlFileName);
	    BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < originUrls.length; i++) {
				String shortenUrlText = u.shortenURL(originUrls[i]);
	            bw.write(originUrls[i] + "\t" + shortenUrlText);
	            v.add(i, originUrls[i] + "\t" + shortenUrlText);
	            bw.newLine();
	            bw.flush();
			}
				
		} catch (Exception e) {
	            e.getStackTrace();
		} finally {
            try {
                if(bw != null) bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
		
		// 1.동일  URL 호출시 Vector에 담은 값과 해당메서드를 통해서 가져온 값과 동일한지 여부를 확인
		try{
			for (int i = 0; i < originUrls.length; i++) {
				String vectorShortenUrl = ((String)v.get(i)).split("\t")[1];
				String methodShortenUrl = u.existShortenUrl(originUrls[i]);
				System.out.println("2-1: " + vectorShortenUrl + " = " + methodShortenUrl);
				if(!vectorShortenUrl.equals(methodShortenUrl)) {
					fail("2-1.Fail to exist shorten url Check!!");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("2-2.Fail to exist shorten url Check!!");
		}
		
	}

	/**
	 * 단축URL의 ID로 원래 URL을 가져오는지 여부
	 */
	@Test
	public void testExistOriginUrl() {
		ShortenUtil u = new ShortenUtil(8, "localhost");
		// 하기와 같이 총 5개의 단축URL을 생성
		String originUrls[] = { "http://www.google.com/",	//1. google : 1개생성
								"http://www.daum.net",   	//2. daum : 1개생성
								"http://www.naver.com",		//3. naver : 1개생성
								"http://www.amazon.com","http://www.amazon.com/page1.php", 		//4. amazon : 2개생성
								};
		// 검증을 위한 배열		
		Vector v = new Vector();
		
		File file = new File(shortenUrlFileDrive,shortenUrlFileName);
	    BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < originUrls.length; i++) {
				String shortenUrlText = u.shortenURL(originUrls[i]);
	            bw.write(originUrls[i] + "\t" + shortenUrlText);
	            v.add(i, originUrls[i] + "\t" + shortenUrlText);
	            bw.newLine();
	            bw.flush();
			}
				
		} catch (Exception e) {
	            e.getStackTrace();
		} finally {
            try {
                if(bw != null) bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
		
		// 1.단축URL의 ID값(locahost/AbCdEfGg => locahost/가 제거된 뒷에
		try{
			for (int i = 0; i < originUrls.length; i++) {
				String vectorOriginUrl = ((String)v.get(i)).split("\t")[0];
				String id = ((String)v.get(i)).split("\t")[1].replace("localhost/", "");
				String methodOriginUrl = u.existOriginUrl(id);
				System.out.println("3-1: " + vectorOriginUrl + " = " + methodOriginUrl);
				if(!vectorOriginUrl.equals(methodOriginUrl)) {
					fail("3-1.Fail to exist origin url search!!");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			fail("3-2.Fail to exist origin url search!!");
		}
	}

}
