package chat;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// ���� JSON�������� ��ȯ�ϰų�, JSON�� ������ ��ȯ�ϴ� ����� ���� Ŭ����
public final class ChatProtocol {

	/*
	 * requestType�� ����
	 * chat(ä��), disconnect(������ thread����), message(�޽��� �˸�),
	 * update(��ǰ ���� �˸�)
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
