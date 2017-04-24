package chat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// 평문을 JSON형식으로 변환하거나, JSON을 평문으로 변환하는 기능을 가진 클래스
public final class ChatProtocol {

	/*
	 * requestType의 종류
	 * chat(채팅), disconnect(서버측 thread종료), message(메시지 알림),
	 * update(물품 입출 알림)
	 */
	public static String toJSON(String requestType, String user_id, String message) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"requestType\":\"" + requestType + "\",");
		sb.append("\"user_id\":\"" + user_id + "\",");
		sb.append("\"message\":\"" + message + "\"");
		sb.append("}");
		return sb.toString();
	}

	public static JSONObject parsing(String json) {
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = null;
		try {
			jsonObj = (JSONObject) parser.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}

}
