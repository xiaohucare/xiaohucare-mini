package com.xiaohucare.codegen.qrcode;

import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

public class QRCodeBitMatrix {
		private final QRCode qrcode;
		private final BitMatrix bitMatrix;
		public QRCodeBitMatrix(QRCode qrcode, BitMatrix bitMatrix) {
			this.qrcode = qrcode;
			this.bitMatrix = bitMatrix;
		}
		public QRCode getQrcode() {
			return qrcode;
		}
		public BitMatrix getBitMatrix() {
			return bitMatrix;
		}
}
