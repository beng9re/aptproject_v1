package aptuser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.krysalis.barcode4j.BarcodeClassResolver;
import org.krysalis.barcode4j.DefaultBarcodeClassResolver;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.MimeTypes;

public class MakeBarcode {

	public MakeBarcode(JPanel panel, String barcodeData, String path) {
		// 바코드 종류와 기타옵션
		String barcodeType = "code128";
		final int dpi = 300;
		boolean isAntiAliasing = false;
		// 파일 출력관련 옵션
		String fileFormat = "png";
		String outputFile = path + "." + fileFormat;

		// 파일출력 프로세스
		FileOutputStream out = null;
		try {
			AbstractBarcodeBean bean = null;
			BarcodeClassResolver resolver = new DefaultBarcodeClassResolver();
			Class cls = resolver.resolveBean(barcodeType);
			bean = (AbstractBarcodeBean) cls.newInstance();
			bean.doQuietZone(true);

			// 파일로 출력준비
			out = new FileOutputStream(new File(outputFile));
			String mimeType = MimeTypes.expandFormat(fileFormat);
			int imageType = BufferedImage.TYPE_BYTE_BINARY;
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, mimeType, dpi, imageType, isAntiAliasing, 0);

			// 바코드 생성
			bean.generateBarcode(canvas, barcodeData);
			canvas.finish();
			JOptionPane.showMessageDialog(panel, "바코드가 생성되었습니다");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}

}
