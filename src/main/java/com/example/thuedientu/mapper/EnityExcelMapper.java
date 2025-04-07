//package com.example.thuedientu.mapper;
//
//import com.example.thuedientu.dto.EnityExcelDTO;
//import com.example.thuedientu.model.EnityExcel;
//
//public class EnityExcelMapper {
//
//    public static EnityExcelDTO toDTO(EnityExcel entity) {
//        EnityExcelDTO dto = new EnityExcelDTO();
//        dto.setId(entity.getId());
//        dto.setTkid(entity.getTkid());
//        dto.setSotk(entity.getSotk());
//        dto.setMahq(entity.getMahq());
//        dto.setTrangthaitk(entity.getTrangthaitk());
//        dto.setBpkthsdt(entity.getBpkthsdt());
//        dto.setBptq(entity.getBptq());
//        dto.setPtvc(entity.getPtvc());
//        dto.setMalh(entity.getMalh());
//        dto.setNgayDk(entity.getNgayDk());
//        dto.setHourDk(entity.getHourDk());
//        dto.setNgayThaydoiDk(entity.getNgayThaydoiDk());
//        dto.setHourThaydoiDk(entity.getHourThaydoiDk());
//        dto.setMasothueKbhq(entity.getMasothueKbhq());
//        dto.setTenDoanhnghiep(entity.getTenDoanhnghiep());
//        dto.setSodienthoai(entity.getSodienthoai());
//        dto.setTenDoanhnghiepUythac(entity.getTenDoanhnghiepUythac());
//        dto.setTenDoitacnuocngoai(entity.getTenDoitacnuocngoai());
//        dto.setMaquocgiaDoitacnuocngoai(entity.getMaquocgiaDoitacnuocngoai());
//        dto.setVandon01(entity.getVandon01());
//        dto.setVandon02(entity.getVandon02());
//        dto.setVandon03(entity.getVandon03());
//        dto.setVandon04(entity.getVandon04());
//        dto.setVandon05(entity.getVandon05());
//        dto.setSoluongkienhang(entity.getSoluongkienhang());
//        dto.setMaDvtKienhang(entity.getMaDvtKienhang());
//        dto.setGrossweight(entity.getGrossweight());
//        dto.setMaDvtGw(entity.getMaDvtGw());
//        dto.setSoluongContainer(entity.getSoluongContainer());
//        dto.setMaDiadiemdohang(entity.getMaDiadiemdohang());
//        dto.setMaDiadiemxephang(entity.getMaDiadiemxephang());
//        dto.setTenPhuongtienvanchuyen(entity.getTenPhuongtienvanchuyen());
//        dto.setNgayHangDen(entity.getNgayHangDen());
//        dto.setPhuongThucThanhToan(entity.getPhuongThucThanhToan());
//        dto.setTongTriGiaHoaDon(entity.getTongTriGiaHoaDon());
//        dto.setTongTriGiaTinhThue(entity.getTongTriGiaTinhThue());
//        dto.setTongTienThue(entity.getTongTienThue());
//        dto.setTongSoDonghang(entity.getTongSoDonghang());
//        dto.setNgayCapPhep(entity.getNgayCapPhep());
//        dto.setGioCapPhep(entity.getGioCapPhep());
//        dto.setNgayHoanthanhKiemtra(entity.getNgayHoanthanhKiemtra());
//        dto.setGioHoanthanhKiemtra(entity.getGioHoanthanhKiemtra());
//        dto.setNgayHuyTk(entity.getNgayHuyTk());
//        dto.setGioHuyTk(entity.getGioHuyTk());
//        dto.setTenNguoiphutrachKiemtrahoso(entity.getTenNguoiphutrachKiemtrahoso());
//        dto.setTenNguoiphutrachKiemhoa(entity.getTenNguoiphutrachKiemhoa());
//        dto.setHsCode(entity.getHsCode());
//        dto.setMoTaHangHoa(entity.getMoTaHangHoa());
//        dto.setSoLuongHanghoa(entity.getSoLuongHanghoa());
//        dto.setMaDvtHanghoa(entity.getMaDvtHanghoa());
//        dto.setTriGiaHoaDon(entity.getTriGiaHoaDon());
//        dto.setDongiaHoadon(entity.getDongiaHoadon());
//        dto.setMaTienteHoadon(entity.getMaTienteHoadon());
//        dto.setDonviDongiaTiente(entity.getDonviDongiaTiente());
//        dto.setTriGiaTinhThueS(entity.getTriGiaTinhThueS());
//        dto.setTriGiaTinhThueM(entity.getTriGiaTinhThueM());
//        dto.setDongiaTinhthue(entity.getDongiaTinhthue());
//        dto.setThuesuatNhapkhau(entity.getThuesuatNhapkhau());
//        dto.setTienThueNhapkhau(entity.getTienThueNhapkhau());
//        dto.setXuatxu(entity.getXuatxu());
//        dto.setMaVanbanphapquy(entity.getMaVanbanphapquy());
//        dto.setPhanloaiGiayphepNk(entity.getPhanloaiGiayphepNk());
//        dto.setMaBieuthueNk(entity.getMaBieuthueNk());
//        dto.setMaMiengiamThue(entity.getMaMiengiamThue());
//        return dto;
//    }
//}
