package com.beermartket.alcohol.rest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.beermartket.alcohol.constant.SessionAttr;
import com.beermartket.alcohol.model.ChiTietPhieuNhapHang;
import com.beermartket.alcohol.model.PhieuNhapHang;
import com.beermartket.alcohol.model.SanPham;
import com.beermartket.alcohol.model.TaiKhoanAdmin;
import com.beermartket.alcohol.repository.CTPhieuNhapHangReponsitory;
import com.beermartket.alcohol.repository.NhaCungCapReponsitory;
import com.beermartket.alcohol.repository.PhieuNhapHangReponsitory;
import com.beermartket.alcohol.repository.TaiKhoanAdminRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@RestController
public class PhieuNhapHang_restController {

	@Autowired
	PhieuNhapHangReponsitory phieuDao;

	@Autowired
	CTPhieuNhapHangReponsitory ctphieuDao;

	@Autowired
	NhaCungCapReponsitory nccDAO;

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	HttpSession session;

	@Autowired
	TaiKhoanAdminRepository taikhoanDAOAdmin;

	@GetMapping("/rest/phieuhang")
	public ResponseEntity<List<PhieuNhapHang>> findAll() {
		return ResponseEntity.ok(phieuDao.findAll());
	}

	@GetMapping("/rest/phieuhang/{maPhieuNhap}")
	public ResponseEntity<List<ChiTietPhieuNhapHang>> getCTPhieuNhap(@PathVariable Integer maPhieuNhap) {
		List<ChiTietPhieuNhapHang> phieus = ctphieuDao.findByMaPhieuNhap(maPhieuNhap);

		if (phieus != null) {
			return ResponseEntity.ok(phieus);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@PutMapping("/update/phieuhang/{ma}")
	public ResponseEntity<PhieuNhapHang> updateCTPhieuNhap(@PathVariable int ma, @RequestBody String ghichu) {
		PhieuNhapHang phieu = phieuDao.findById(ma).orElse(null);
		phieu.setGhiChu(ghichu);
		return ResponseEntity.ok(phieuDao.save(phieu));
	}

	@PutMapping("/update/phieuhang/trangthai/{ma}")
	public ResponseEntity<PhieuNhapHang> updateTTPhieuNhap2(@PathVariable int ma, @RequestBody String ghichu) {
		PhieuNhapHang phieu = phieuDao.findById(ma).orElse(null);
		phieu.setTrangThai("3");
		if (phieu.getGhiChu() == null) {
			phieu.setGhiChu(ghichu);
		} else {
			phieu.setGhiChu(phieu.getGhiChu() + " - Lý do hủy phiếu hàng: " + ghichu);
		}
		sendVerificationCodeByEmailHuy(phieu.getNhaCungCap().getEmail(), phieu.getMaPhieuNhap(), ghichu);
		return ResponseEntity.ok(phieuDao.save(phieu));
	}

	@PutMapping("/update/phieuhang/trangThai/{ma}")
	public ResponseEntity<PhieuNhapHang> updateTTPhieuNhap(@PathVariable int ma) {
		PhieuNhapHang phieu = phieuDao.findById(ma).orElse(null);

		if (phieu != null) {
			// Giả sử "2" là trạng thái "Đã nhận hàng"
			phieu.setTrangThai("2");

			// Cập nhật trạng thái của các mục ChiTietPhieuNhapHang
			for (ChiTietPhieuNhapHang chiTiet : phieu.getChiTietPhieuNhapHangs()) {
				// Cập nhật trạng thái của sản phẩm
				chiTiet.getSanPham().setSoLuong(chiTiet.getSanPham().getSoLuong() + chiTiet.getSoLuongNhap());
			}

			return ResponseEntity.ok(phieuDao.save(phieu));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/add/phieunhaphang/{maNhaCungCap}")
	public ResponseEntity<PhieuNhapHang> createPhieuNhapHang(

			@RequestBody List<SanPham> sanphamdachon, @PathVariable Integer maNhaCungCap) {
		PhieuNhapHang phieu = new PhieuNhapHang();

		// Lấy thời gian hiện tại
		LocalDateTime currentTime = LocalDateTime.now();
		// Chuyển đổi thành kiểu Timestamp
		Timestamp currentTimestamp = Timestamp.valueOf(currentTime);

		TaiKhoanAdmin tk = new TaiKhoanAdmin();

		String tendangnhap = (String) session.getAttribute(SessionAttr.Admin);
		if (tendangnhap != null) {
			tk = taikhoanDAOAdmin.findByTenDangNhap(tendangnhap);

		}
		// Gán thời gian hiện tại cho trường NgayTao
		phieu.setNgayNhap(currentTimestamp);
		phieu.setTaiKhoanAdmin(tk);
		phieu.setTrangThai("1");
		phieu.setNhaCungCap(nccDAO.findById(maNhaCungCap).orElse(null));
		float tongtien = 0;
		for (SanPham sp : sanphamdachon) {
			tongtien += sp.getGiaNhap();
		}

		phieu.setTongTien(tongtien);
		phieuDao.save(phieu);

		// Lưu thông tin chi tiết phiếu nhập hàng từ danh sách sản phẩm đã chọn
		for (SanPham sp : sanphamdachon) {
			ChiTietPhieuNhapHang ctphieu = new ChiTietPhieuNhapHang();
			ctphieu.setSanPham(sp);
			ctphieu.setSoLuongNhap(sp.getSoLuong());
			ctphieu.setGiaNhap(sp.getGiaNhap());
			ctphieu.setPhieuNhapHang(phieu);
			ctphieuDao.save(ctphieu);
		}
		sendVerificationCodeByEmail(phieu.getNhaCungCap().getEmail(), phieu.getMaPhieuNhap());

		return ResponseEntity.ok(phieu);
	}

	private void sendVerificationCodeByEmail(String toEmail, int maPhieuHang) {
		int ma = maPhieuHang;
		PhieuNhapHang pn = phieuDao.findById(ma).orElse(null);
		
		List<ChiTietPhieuNhapHang> ctPhieu = ctphieuDao.findByMaPhieuNhap(ma);

		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			String Subject = "BEE MARKET XIN CHÀO, CẢM ƠN BẠN ĐÃ HỢP TÁC VỚI BEE MARKET";

			StringBuilder ctphieuHTML = new StringBuilder();

			for (ChiTietPhieuNhapHang ct : ctPhieu) {
				ctphieuHTML.append("<tr>");
				ctphieuHTML.append("<td style=\"text-align: center; padding: 10px;\">").append("<img src=\""
						+ ct.getSanPham().getHinhAnh()
						+ "\" alt=\"Hình ảnh sản phẩm\" style=\"max-width: 100%; height: auto; border-radius: 50%; display: block; margin: 0 auto; max-width: 50px; max-height: 50px;\">")
						.append("</td>");
				ctphieuHTML.append("<td style=\"text-align: center;\">")
						.append(ct.getSanPham().getTenSanPham()).append("</td> ");
				ctphieuHTML.append("<td style=\"text-align: center; padding: 10px;\">").append(ct.getGiaNhap() + " đ")
						.append("</td> ");
				ctphieuHTML.append("<td style=\"text-align: center; padding: 10px;\">").append(ct.getSoLuongNhap())
						.append("</td> ");
				ctphieuHTML.append("<td style=\"text-align: center; padding: 10px;\">")
						.append(ct.getSoLuongNhap() * ct.getGiaNhap() + " đ").append("</td>");
				ctphieuHTML.append("</tr>");
			}

			Timestamp ngayData = (Timestamp) pn.getNgayNhap();
			// Định dạng theo yêu cầu (vd: "HH:mm:ss dd-MM-yyyy")
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
			String ngayDaFormat = sdf.format(ngayData);
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
					+ "                                                            style=\"color: #4d79f6; font-weight: 700;\">Doanh\r\n"
					+ "                                                            nghiệp</span>.</h2>\r\n"
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
					+ "                                                                <br />Ngày " + ngayDaFormat
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

	private void sendVerificationCodeByEmailHuy(String toEmail, int maPhieuHang, String ghichu) {
		int ma = maPhieuHang;
		PhieuNhapHang pn = phieuDao.findById(ma).orElse(null);

		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			String Subject = "BEE MARKET XIN CHÀO, CẢM ƠN BẠN ĐÃ HỢP TÁC VỚI BEE MARKET";

			String lydo = ghichu;
			Timestamp ngayData = (Timestamp) pn.getNgayNhap();
			// Định dạng theo yêu cầu (vd: "HH:mm:ss dd-MM-yyyy")
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
			String ngayDaFormat = sdf.format(ngayData);
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
					+ "                                                                Mã Hóa Đơn Nhập: #" + ma + "\r\n"
					+ "                                                                <br />Ngày " + ngayDaFormat
					+ " <h4>Xin rất tiếc chúng tôi hủy đơn hàng vì lý do: </h4> " + ghichu + "\r\n"
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

}
