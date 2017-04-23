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
		// ���ڵ� ������ ��Ÿ�ɼ�
		String barcodeType = "code128";
		final int dpi = 300;
		boolean isAntiAliasing = false;
		// ���� ��°��� �ɼ�
		String fileFormat = "png";
		String outputFile = path + "." + fileFormat;

		// ������� ���μ���
		FileOutputStream out = null;
		try {
			AbstractBarcodeBean bean = null;
			BarcodeClassResolver resolver = new DefaultBarcodeClassResolver();
			Class cls = resolver.resolveBean(barcodeType);
			bean = (AbstractBarcodeBean) cls.newInstance();
			bean.doQuietZone(true);

			// ���Ϸ� ����غ�
			out = new FileOutputStream(new File(outputFile));
			String mimeType = MimeTypes.expandFormat(fileFormat);
			int imageType = BufferedImage.TYPE_BYTE_BINARY;
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, mimeType, dpi, imageType, isAntiAliasing, 0);

			// ���ڵ� ����
			bean.generateBarcode(canvas, barcodeData);
			canvas.finish();
			JOptionPane.showMessageDialog(panel, "���ڵ尡 �����Ǿ����ϴ�");

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
