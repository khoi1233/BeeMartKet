package com.beermartket.alcohol.rest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.model.ChiTietHoaDon;
import com.beermartket.alcohol.model.ChiTietPhieuNhapHang;
import com.beermartket.alcohol.model.HoaDon;
import com.beermartket.alcohol.model.PhieuNhapHang;
import com.beermartket.alcohol.repository.ChiTietHoaDonReponsitory;
import com.beermartket.alcohol.repository.HoaDonReponsitory;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@RestController
public class HoaDon_restController {

	@Autowired
	HoaDonReponsitory hoaDonDao;

	@Autowired
	ChiTietHoaDonReponsitory hoaDonCTDao;

	@Autowired
	private JavaMailSender javaMailSender;

	@GetMapping("/rest/hoadon")
	public ResponseEntity<List<HoaDon>> findAll() {
		return ResponseEntity.ok(hoaDonDao.findAll());
	}

	@GetMapping("/rest/xacnhanhoadon")
	public ResponseEntity<List<HoaDon>> findByTrangThaiHD() {
		return ResponseEntity.ok(hoaDonDao.findByTrangThaiHoaDon());
	}

	@GetMapping("/rest/hoadonxacnhan")
	public ResponseEntity<List<HoaDon>> findByTrangThaiHD2() {
		return ResponseEntity.ok(hoaDonDao.findByTrangThaiHoaDon2());
	}

	@GetMapping("/rest/hoadondahuy")
	public ResponseEntity<List<HoaDon>> findByTrangThaiHD3() {
		return ResponseEntity.ok(hoaDonDao.findByTrangThaiHoaDon3());
	}

	@GetMapping("/hoadon/{maHoaDon}")
	public HoaDon getHoaDonByMaHoaDon(@PathVariable int maHoaDon) {
		Optional<HoaDon> optionalHoaDon = hoaDonDao.findById(maHoaDon);
		if (optionalHoaDon.isPresent()) {
			return optionalHoaDon.get();
		} else {
			// Nếu không tìm thấy hóa đơn, có thể trả về một response HTTP 404 Not Found.
			// Hoặc bạn có thể trả về một giá trị mặc định hoặc làm bất kỳ xử lý nào phù hợp
			// với ứng dụng của bạn.
			return null;
		}
	}

	@GetMapping("/rest/hoadon/thoigian/{maHoaDon}")
	public ResponseEntity<Integer> tinhThoiGian(@PathVariable Integer maHoaDon) {
		Integer thoigian = hoaDonDao.findTimeDifferenceByMaHoaDon(maHoaDon);
		return ResponseEntity.ok(thoigian);
	}

	@GetMapping("/rest/chitiet/{maHoaDon}")
	public ResponseEntity<List<ChiTietHoaDon>> findByID(@PathVariable Integer maHoaDon) {
		List<ChiTietHoaDon> chiTietHoaDons = hoaDonCTDao.findByMaHoaDon(maHoaDon);
		return ResponseEntity.ok(chiTietHoaDons);
	}

	@PutMapping("/rest/{maHoaDon}/cap-nhat-trang-thai/{trangThaiHoaDon}")
	public ResponseEntity<HoaDon> capNhatTrangThaiHoaDon(@PathVariable int maHoaDon,
			@PathVariable String trangThaiHoaDon, @RequestBody String ghichu) {
		try {
			Optional<HoaDon> hoaDonOptional = hoaDonDao.findById(maHoaDon);

			if (!hoaDonOptional.isPresent()) {
				return ResponseEntity.notFound().build();
			}
			HoaDon hoaDon = hoaDonOptional.get();
			hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
			hoaDon.setGhiChu(ghichu);
			
			if (hoaDon.getTaiKhoan().getEmail() != null) {
				sendVerificationCodeByEmailHuy(hoaDon.getTaiKhoan().getEmail(), hoaDon.getMaHoaDon(), hoaDon.getGhiChu());
			}
			
			hoaDonDao.save(hoaDon);

			return ResponseEntity.ok(hoaDon);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PutMapping("/rest/{maHoaDon}/cap-nhat-trang-thai2/{trangThaiHoaDon}")
	public ResponseEntity<HoaDon> capNhatTrangThaiHoaDon2(@PathVariable int maHoaDon,
			@PathVariable String trangThaiHoaDon) {
		try {
			Optional<HoaDon> hoaDonOptional = hoaDonDao.findById(maHoaDon);

			if (!hoaDonOptional.isPresent()) {
				return ResponseEntity.notFound().build();
			}
			HoaDon hoaDon = hoaDonOptional.get();
			hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);		
			
			hoaDonDao.save(hoaDon);
			if (hoaDon.getTaiKhoan().getEmail() != null) {
				 sendVerificationCodeByEmail(hoaDon.getTaiKhoan().getEmail(), hoaDon.getMaHoaDon());
			}
           
			return ResponseEntity.ok(hoaDon);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	private void sendVerificationCodeByEmailHuy(String toEmail, int maHoaDon, String ghichu) {

		HoaDon pn = hoaDonDao.findById(maHoaDon).orElse(null);

		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			String Subject = "BEE MARKET XIN CHÀO, CẢM ƠN BẠN ĐÃ HỢP TÁC VỚI BEE MARKET";

			String lydo = ghichu;
			LocalDateTime gioHienTai = LocalDateTime.now(); // Định dạng theo yêu cầu
			// Định dạng theo yêu cầu (dd-MM-yyyy HH:mm:ss)
	        DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	        // Áp dụng định dạng
	        String gioDinhDang = gioHienTai.format(dinhDang);
			String content = "<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "\r\n" + "\r\n"
					+ "<!-- Mirrored from mannatthemes.com/dastone/default/email-templates-billing.html by HTTrack Website Copier/3.x [XR&CO'2014], Mon, 06 Nov 2023 06:55:02 GMT -->\r\n"
					+ "\r\n" + "<head>\r\n" + "    <meta charset=\"utf-8\" />\r\n"
					+ "    <title>Dastone - Admin & Dashboard Template</title>\r\n"
					+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\r\n"
					+ "    <meta content=\"Premium Multipurpose Admin & Dashboard Template\" name=\"description\" />\r\n"
					+ "    <meta content=\"\" name=\"author\" />\r\n"
					+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n" + "\r\n" + "    " + "\r\n"
					+ "\r\n" + "</head>\r\n" + "\r\n" + "<body>\r\n" + "\r\n" + "\r\n" + "    <!-- Page-Title -->\r\n"
					+ "    <div class=\"page-wrapper d-flex justify-content-center\" >\r\n"
					+ "            <!-- Page Content-->\r\n" + "            <div class=\"page-content\">\r\n"
					+ "                <div class=\"container-fluid\"> <div class=\"row\">\r\n"
					+ "        <div class=\"col-lg-12\">\r\n" + "            <table class=\"body-wrap\"\r\n"
					+ "                style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; width: 100%; background-color: transparent; margin: 0;\"\r\n"
					+ "                bgcolor=\"transparent\">\r\n" + "                <tr>\r\n"
					+ "                    <td valign=\"top\"></td>\r\n"
					+ "                    <td class=\"container\" width=\"600\"\r\n"
					+ "                        style=\"display: block !important; max-width: 600px !important; clear: both !important;\"\r\n"
					+ "                        valign=\"top\">\r\n"
					+ "                        <div class=\"content\" style=\"padding: 20px;\">\r\n"
					+ "                            <table class=\"main\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
					+ "                                style=\"border: 1px dashed #4d79f6;\">\r\n"
					+ "                                <tr>\r\n"
					+ "                                    <td class=\"content-wrap aligncenter\"\r\n"
					+ "                                        style=\"padding: 20px; background-color: transparent;\" align=\"center\"\r\n"
					+ "                                        valign=\"top\">\r\n"
					+ "                                        <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
					+ "                                            <tr>\r\n"
					+ "                                                <td>\r\n"
					+ "                                                    <a href=\"#\"><img\r\n"
					+ "                                                            src=\"https://firebasestorage.googleapis.com/v0/b/formal-facet-395516.appspot.com/o/logomoi.png?alt=media&token=801711dc-776f-4e39-9750-d6f3983a57c1\"\r\n"
					+ "                                                            alt=\"\"\r\n"
					+ "                                                            style=\"height: 130px; margin-left: auto; margin-right: auto; display:block;\"></a>\r\n"
					+ "                                                </td>\r\n"
					+ "                                            </tr>\r\n"
					+ "                                            <tr>\r\n"
					+ "                                                <td class=\"content-block\" style=\"padding: 0 0 20px;\" valign=\"top\">\r\n"
					+ "                                                    <h2 class=\"aligncenter\"\r\n"
					+ "                                                        style=\"font-family: 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif;font-size: 24px; color:#50649c; line-height: 1.2em; font-weight: 600; text-align: center;\"\r\n"
					+ "                                                        align=\"center\">Chúng tôi xin cảm ơn <span\r\n"
					+ "                                                            style=\"color: #4d79f6; font-weight: 700;\">Doanh\r\n"
					+ "                                                            nghiệp</span>.</h2>\r\n"
					+ "                                                </td>\r\n"
					+ "                                            </tr>\r\n"
					+ "                                            <tr>\r\n"
					+ "                                                <td class=\"content-block aligncenter\" style=\"padding: 0 0 20px;\"\r\n"
					+ "                                                     valign=\"top\">\r\n"
					+ "                                                    <table>\r\n"
					+ "                                                        <thead><tr>\r\n"
					+ "                                                            <td><h3>Lý Do Hủy Đơn Hàng</h3>\r\n"
					+ "                                                         \r\n"
					+ "                                                                Mã Hóa Đơn Nhập: #" + maHoaDon
					+ "\r\n"

					+ "                                                                <br />Ngày " + gioDinhDang

					+ " <h4>Xin rất tiếc chúng tôi hủy đơn hàng vì lý do </h4> " + lydo + "\r\n"
					+ "                                                            </td>\r\n"
					+ "                                                        </tr><thead>\r\n"
					+ "                            </table><!--end table-->\r\n"
					+ "                        </div><!--end content-->\r\n" + "                    </td>\r\n"
					+ "                    <td style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; font-size: 14px; vertical-align: top; margin: 0;\"\r\n"
					+ "                        valign=\"top\"></td>\r\n" + "                </tr>\r\n"
					+ "            </table><!--end table-->\r\n" + "        </div><!--end col-->\r\n"
					+ "    </div><!--end row-->\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n"
					+ "                </div><!-- container -->\r\n" + "\r\n" + "                 \r\n"
					+ "            </div>\r\n" + "            <!-- end page content -->\r\n" + "        </div>\r\n"
					+ "        <!-- end page-wrapper --></body>\r\n" + "\r\n" + "\r\n"
					+ "<!-- Mirrored from mannatthemes.com/dastone/default/email-templates-billing.html by HTTrack Website Copier/3.x [XR&CO'2014], Mon, 06 Nov 2023 06:55:02 GMT -->\r\n"
					+ "\r\n" + "</html>";
			helper.setTo(toEmail);
			helper.setSubject(Subject);
			helper.setText(content, true); // Sử dụng true để cho phép email hiển thị HTML

			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		javaMailSender.send(message);

	}
	
	private void sendVerificationCodeByEmail(String toEmail, int maHoaDon) {

	int ma = maHoaDon;
	HoaDon pn = hoaDonDao.findById(ma).orElse(null);
	
	List<ChiTietHoaDon> ctPhieu = hoaDonCTDao.findByMaHoaDon(maHoaDon);

	MimeMessage message = javaMailSender.createMimeMessage();
	try {
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		String Subject = "BEE MARKET XIN CHÀO, CẢM ƠN BẠN ĐÃ HỢP TÁC VỚI BEE MARKET";

		StringBuilder ctphieuHTML = new StringBuilder();
        String tenKH = pn.getTaiKhoan().getHoTen();
		for (ChiTietHoaDon ct : ctPhieu) {
			ctphieuHTML.append("<tr>");
			ctphieuHTML.append("<td style=\"text-align: center; padding: 10px;\">").append("<img src=\""
					+ ct.getSanPham().getHinhAnh()
					+ "\" alt=\"Hình ảnh sản phẩm\" style=\"max-width: 100%; height: auto; border-radius: 50%; display: block; margin: 0 auto; max-width: 50px; max-height: 50px;\">")
					.append("</td>");
			ctphieuHTML.append("<td style=\"text-align: center;\">")
					.append(ct.getSanPham().getTenSanPham()).append("</td> ");
			ctphieuHTML.append("<td style=\"text-align: center; padding: 10px;\">").append(ct.getGiaBan() + " đ")
					.append("</td> ");
			ctphieuHTML.append("<td style=\"text-align: center; padding: 10px;\">").append(ct.getSoLuong())
					.append("</td> ");
			ctphieuHTML.append("<td style=\"text-align: center; padding: 10px;\">")
					.append(ct.getGiaBan() * ct.getSoLuong() + " đ").append("</td>");
			ctphieuHTML.append("</tr>");
		}
		LocalDateTime gioHienTai = LocalDateTime.now(); // Định dạng theo yêu cầu
		// Định dạng theo yêu cầu (dd-MM-yyyy HH:mm:ss)
        DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // Áp dụng định dạng
        String gioDinhDang = gioHienTai.format(dinhDang);
		float tongtien = (float) pn.getTongTien();
		String content = "<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "\r\n" + "\r\n"
				+ "<!-- Mirrored from mannatthemes.com/dastone/default/email-templates-billing.html by HTTrack Website Copier/3.x [XR&CO'2014], Mon, 06 Nov 2023 06:55:02 GMT -->\r\n"
				+ "\r\n" + "<head>\r\n" + "    <meta charset=\"utf-8\" />\r\n"
				+ "    <title>Dastone - Admin & Dashboard Template</title>\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\r\n"
				+ "    <meta content=\"Premium Multipurpose Admin & Dashboard Template\" name=\"description\" />\r\n"
				+ "    <meta content=\"\" name=\"author\" />\r\n"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\r\n" + "\r\n" + "    " + "\r\n"
				+ "\r\n" + "</head>\r\n" + "\r\n" + "<body>\r\n" + "\r\n" + "\r\n" + "    <!-- Page-Title -->\r\n"
				+ "    <div class=\"page-wrapper d-flex justify-content-center\" >\r\n"
				+ "            <!-- Page Content-->\r\n" + "            <div class=\"page-content\">\r\n"
				+ "                <div class=\"container-fluid\"> <div class=\"row\">\r\n"
				+ "        <div class=\"col-lg-12\">\r\n" + "            <table class=\"body-wrap\"\r\n"
				+ "                style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; box-sizing: border-box; font-size: 14px; width: 100%; background-color: transparent; margin: 0;\"\r\n"
				+ "                bgcolor=\"transparent\">\r\n" + "                <tr>\r\n"
				+ "                    <td valign=\"top\"></td>\r\n"
				+ "                    <td class=\"container\" width=\"600\"\r\n"
				+ "                        style=\"display: block !important; max-width: 600px !important; clear: both !important;\"\r\n"
				+ "                        valign=\"top\">\r\n"
				+ "                        <div class=\"content\" style=\"padding: 20px;\">\r\n"
				+ "                            <table class=\"main\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\"\r\n"
				+ "                                style=\"border: 1px dashed #4d79f6;\">\r\n"
				+ "                                <tr>\r\n"
				+ "                                    <td class=\"content-wrap aligncenter\"\r\n"
				+ "                                        style=\"padding: 20px; background-color: transparent;\" align=\"center\"\r\n"
				+ "                                        valign=\"top\">\r\n"
				+ "                                        <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\">\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td>\r\n"
				+ "                                                    <a href=\"#\"><img\r\n"
				+ "                                                            src=\"https://firebasestorage.googleapis.com/v0/b/formal-facet-395516.appspot.com/o/logomoi.png?alt=media&token=801711dc-776f-4e39-9750-d6f3983a57c1\"\r\n"
				+ "                                                            alt=\"\"\r\n"
				+ "                                                            style=\"height: 130px; margin-left: auto; margin-right: auto; display:block;\"></a>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td class=\"content-block\" style=\"padding: 0 0 20px;\" valign=\"top\">\r\n"
				+ "                                                    <h2 class=\"aligncenter\"\r\n"
				+ "                                                        style=\"font-family: 'Helvetica Neue',Helvetica,Arial,'Lucida Grande',sans-serif;font-size: 24px; color:#50649c; line-height: 1.2em; font-weight: 600; text-align: center;\"\r\n"
				+ "                                                        align=\"center\">Chúng tôi xin cảm ơn <span\r\n"
				+ "                                                            style=\"color: #4d79f6; font-weight: 700;\">"+tenKH+"</span>.</h2>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td "
				+ "                                                     >\r\n"
				+ "                                                    <table>\r\n"
				+ "                                                        <thead><tr>\r\n"
				+ "                                                            <td><h4>THÔNG TIN PHIẾU NHẬP</h4>\r\n"
				+ "                                                         \r\n"
				+ "                                                                Mã Hóa Đơn Nhập: #" + ma + "\r\n"
				+ "                                                                <br />Ngày " + gioDinhDang
				+ "\r\n" + "                                                            </td>\r\n"
				+ "                                                        </tr><thead>\r\n"
				+ "                                                        <tr>\r\n"
				+ "                                                            <td" + "<thead>\r\n" + "	<tr>\r\n"
				+ "		<th style=\"padding: 10px;\"></th>\r\n" + "		<th>Tên</th>\r\n"
				+ "		<th style=\"padding: 10px;\">Giá</th>\r\n"
				+ "		<th style=\"padding: 10px;\">SL</th>\r\n"
				+ "		<th style=\"padding: 10px;\">Tổng Tiền</th>\r\n" + "	</tr>\r\n" + "</thead>"
				+ "   <tbody>"

				+ ctphieuHTML.toString() + "   </tbody>" + " </table>\r\n"
				+ "                                                                                                                            <hr>\r\n"
				+ "                                                                                                                            <h4 style=\"text-align: center;\">Tổng tiền: "
				+ tongtien + "</h4>\r\n"
				+ "                                                                                                                            <hr>"
				+ "                                                            </td>\r\n"
				+ "                                                        </tr>\r\n"
				+ "                                                    </table>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td class=\"content-block aligncenter\"\r\n"
				+ "                                                    style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; font-size: 14px; vertical-align: top; text-align: center; margin: 0; padding: 0 0 20px;\"\r\n"
				+ "                                                    align=\"center\" valign=\"top\">\r\n"
				+ "                                                    <a href=\"#\"\r\n"
				+ "                                                        style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; font-size: 14px; color: #4d79f6; text-decoration: underline; margin: 0;\">View\r\n"
				+ "                                                        in browser</a>\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                            <tr>\r\n"
				+ "                                                <td class=\"content-block aligncenter\"\r\n"
				+ "                                                    style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; font-size: 14px; vertical-align: top; text-align: center; margin: 0; padding: 0 0 20px;\"\r\n"
				+ "                                                    align=\"center\" valign=\"top\">\r\n"
				+ "                                                    Dastone Inc. 123 my street, And my Country\r\n"
				+ "                                                </td>\r\n"
				+ "                                            </tr>\r\n"
				+ "                                        </table><!--end table-->\r\n"
				+ "                                    </td>\r\n" + "                                </tr>\r\n"
				+ "                            </table><!--end table-->\r\n"
				+ "                        </div><!--end content-->\r\n" + "                    </td>\r\n"
				+ "                    <td style=\"font-family: 'Helvetica Neue',Helvetica,Arial,sans-serif; font-size: 14px; vertical-align: top; margin: 0;\"\r\n"
				+ "                        valign=\"top\"></td>\r\n" + "                </tr>\r\n"
				+ "            </table><!--end table-->\r\n" + "        </div><!--end col-->\r\n"
				+ "    </div><!--end row-->\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n" + "\r\n"
				+ "                </div><!-- container -->\r\n" + "\r\n" + "                 \r\n"
				+ "            </div>\r\n" + "            <!-- end page content -->\r\n" + "        </div>\r\n"
				+ "        <!-- end page-wrapper --></body>\r\n" + "\r\n" + "\r\n"
				+ "<!-- Mirrored from mannatthemes.com/dastone/default/email-templates-billing.html by HTTrack Website Copier/3.x [XR&CO'2014], Mon, 06 Nov 2023 06:55:02 GMT -->\r\n"
				+ "\r\n" + "</html>";
		helper.setTo(toEmail);
		helper.setSubject(Subject);
		helper.setText(content, true); // Sử dụng true để cho phép email hiển thị HTML

		javaMailSender.send(message);
	} catch (MessagingException e) {
		e.printStackTrace();
	}
}

}
