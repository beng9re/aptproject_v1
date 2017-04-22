package aptuser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.krysalis.barcode4j.BarcodeClassResolver;
import org.krysalis.barcode4j.DefaultBarcodeClassResolver;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.MimeTypes;

public class MakeBarcode {

	public MakeBarcode() {
		String barcodeType = "code128";
		String barcodeData = "admin";
		final int dpi = 203;
		boolean isAntiAliasing = false;
		String fileFormat = "png";

		String dir = "d:/";
		String fileName = "barcodetest_" + barcodeType;
		String outputFile = dir + fileName + "." + fileFormat;

		try {
			AbstractBarcodeBean bean = null;

			BarcodeClassResolver resolver = new DefaultBarcodeClassResolver();
			Class clazz = resolver.resolveBean(barcodeType);
			bean = (AbstractBarcodeBean) clazz.newInstance();
			bean.doQuietZone(true);

			// ���Ϸ� ����غ�
			OutputStream out = new FileOutputStream(new File(outputFile));
			try {
				String mimeType = MimeTypes.expandFormat(fileFormat);
				int imageType = BufferedImage.TYPE_BYTE_BINARY;
				BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, mimeType, dpi, imageType, isAntiAliasing,
						0);

				// ���ڵ� ����
				bean.generateBarcode(canvas, barcodeData);
				canvas.finish();

				System.out.println("���ڵ� ���� ������");
			} finally {
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
