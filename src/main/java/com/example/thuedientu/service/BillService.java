//package com.example.thuedientu.service;
//
//import com.example.thuedientu.dto.BillDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class BillService {
//
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    public List<BillDTO> searchBill(Date fromDate, Date toDate, int page, int pageSize) {
//        int offset = (page - 1) * pageSize;
//
//        String sql = """
//        ;WITH
//        FilteredTokhai AS (
//            SELECT vandon_01, sotk, mo_Ta_Hang_Hoa, tkid, ngay_Dk
//            FROM thue_nhap_khau
//            WHERE ngay_Dk BETWEEN ? AND ?
//        ),
//        FilteredHouse AS (
//            SELECT *
//            FROM seaway_house_bill
//            WHERE LEN(ISNULL(MasterBillNo,'')) >= 1
//        ),
//        FilteredMaster AS (
//            SELECT C.*
//            FROM seaway_master_bill C
//            WHERE EXISTS (
//                SELECT 1
//                FROM FilteredHouse H
//                WHERE H.MasterBillNo = C.BillNumber
//            )
//        )
//        SELECT
//            A.vandon_01,
//            A.sotk,
//            A.mo_Ta_Hang_Hoa,
//            A.tkid,
//            A.ngay_Dk,
//            B.MasterBillNo,
//            C.ArrivalDate,
//            B.ContNumber,
//            B.GoodDescription,
//            B.id AS house_id,
//            C.BillNumber,
//            C.ArrivalDate,
//            C.MoTaHangHoa,
//            C.ContNumber,
//            C.id AS master_id
//        FROM FilteredTokhai AS A
//        JOIN FilteredHouse AS B ON CHARINDEX(B.MasterBillNo, A.vandon_01) > 0
//        JOIN FilteredMaster AS C ON C.BillNumber = B.MasterBillNo AND C.ContNumber = B.ContNumber
//        ORDER BY A.tkid
//        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
//    """;
//
//        return jdbcTemplate.query(
//                sql,
//                new Object[]{fromDate, toDate, offset, pageSize},
//                (rs, rowNum) -> {
//                    BillDTO dto = new BillDTO();
//                    dto.setVandon01(rs.getString("vandon_01"));
//                    dto.setSotk(rs.getString("sotk"));
//                    dto.setMoTaHangHoaTokhai(rs.getString("mo_Ta_Hang_Hoa"));
//                    dto.setTkid(rs.getString("tkid"));
//                    dto.setNgayDk(rs.getDate("ngay_Dk"));
//                    dto.setMasterBillNo(rs.getString("MasterBillNo"));
//                    dto.setArrivalDate(rs.getDate("ArrivalDate"));
//                    dto.setContNumber(rs.getString("ContNumber"));
//                    dto.setGoodDescription(rs.getString("GoodDescription"));
//                    dto.setHouseId(rs.getInt("house_id"));
//                    dto.setBillNumber(rs.getString("BillNumber"));
//                    dto.setMoTaHangHoaMaster(rs.getString("MoTaHangHoa"));
//                    dto.setMasterId(rs.getInt("master_id"));
//                    return dto;
//                }
//        );
//    }
//
//}
//
//
////DECLARE @page INT = 19;
////DECLARE @pageSize INT = 10;
////
////SELECT
////A.vandon_01, A.sotk, A.mo_Ta_Hang_Hoa, A.tkid,
////B.MasterBillNo, B.ContNumber, B.GoodDescription, B.id AS house_id,
////C.BillNumber, C.MoTaHangHoa, C.ContNumber, C.id AS master_id
////FROM thue_nhap_khau A
////JOIN seaway_house_bill B
////ON CHARINDEX(B.MasterBillNo, A.vandon_01) > 0
////JOIN seaway_master_bill C
////ON C.BillNumber = B.MasterBillNo AND B.ContNumber = C.ContNumber
////ORDER BY A.tkid
////OFFSET (@page - 1) * @pageSize ROWS
////FETCH NEXT @pageSize ROWS ONLY;
//
//
////
////DECLARE @page       INT  = 1;
////DECLARE @pageSize   INT  = 10;
////DECLARE @fromDate   DATE = '2024-10-01';
////DECLARE @toDate     DATE = '2024-10-02';
////
////;WITH  -- 1. Tờ khai đã giới hạn ngày
////FilteredTokhai AS (
////        SELECT vandon_01 , sotk , mo_Ta_Hang_Hoa , tkid , ngay_Dk
////                FROM   thue_nhap_khau
////                WHERE  ngay_Dk BETWEEN @fromDate AND @toDate
////),
////
////-- 2. House-bill chỉ lấy mã ≥ 2 ký tự
////FilteredHouse AS (
////        SELECT *
////                FROM   seaway_house_bill
////                WHERE  LEN(ISNULL(MasterBillNo,'')) >= 2
////        ),
////
////        -- 3. Master-bill tương ứng (giảm bớt dữ liệu C)
////FilteredMaster AS (
////        SELECT  C.*
////                FROM    seaway_master_bill C
////                WHERE   EXISTS ( SELECT 1
////                FROM FilteredHouse H
////                WHERE H.MasterBillNo = C.BillNumber )
////)
////
////SELECT
////A.vandon_01 ,
////A.sotk ,
////A.mo_Ta_Hang_Hoa ,
////A.tkid ,
////A.ngay_Dk ,
////B.MasterBillNo ,
////B.ContNumber ,
////B.GoodDescription ,
////B.id  AS house_id ,
////C.BillNumber ,
////C.MoTaHangHoa ,
////C.ContNumber ,
////C.id AS master_id
////FROM   FilteredTokhai  AS A
////JOIN   FilteredHouse   AS B
////ON CHARINDEX(B.MasterBillNo, A.vandon_01) > 3
////JOIN   FilteredMaster  AS C
////ON  C.BillNumber = B.MasterBillNo
////AND C.ContNumber  = B.ContNumber
////ORDER BY A.tkid
////OFFSET (@page - 1) * @pageSize ROWS
////FETCH  NEXT @pageSize  ROWS ONLY;
//
//
//
//
//
//
////
////DECLARE @page       INT  = 1;
////DECLARE @pageSize   INT  = 1000;
////DECLARE @fromDate   DATE = '2024-10-01';
////DECLARE @toDate     DATE = '2024-10-02';
////DECLARE @fromDate1   DATE = '2024-11-01';
////DECLARE @toDate1     DATE = '2024-9-02';
////
////
////
////;WITH  -- 1. Tờ khai đã giới hạn ngày
////FilteredTokhai AS (
////        SELECT vandon_01 , sotk , mo_Ta_Hang_Hoa , tkid , ngay_Dk
////                FROM   thue_nhap_khau
////                WHERE  ngay_Dk BETWEEN @fromDate AND @toDate
////),
////
////-- 2. House-bill chỉ lấy mã ≥ 2 ký tự
////FilteredHouse AS (
////        SELECT *
////                FROM   seaway_house_bill
////                WHERE  LEN(ISNULL(MasterBillNo,'')) >= 1
////        ),
////
////        -- 3. Master-bill tương ứng (giảm bớt dữ liệu C)
////FilteredMaster AS (
////        SELECT  C.*
////                FROM    seaway_master_bill C
////                WHERE   EXISTS ( SELECT 1
////                FROM FilteredHouse H
////                WHERE H.MasterBillNo = C.BillNumber )
////)
////
////SELECT
////A.vandon_01 ,
////A.sotk ,
////A.mo_Ta_Hang_Hoa ,
////A.tkid ,
////A.ngay_Dk ,
////B.MasterBillNo ,
////C.ArrivalDate,
////B.ContNumber ,
////B.GoodDescription ,
////B.id  AS house_id ,
////C.BillNumber ,
////C.ArrivalDate,
////C.MoTaHangHoa ,
////C.ContNumber ,
////C.id AS master_id
////FROM   FilteredTokhai  AS A
////JOIN   FilteredHouse   AS B
////ON CHARINDEX(B.MasterBillNo, A.vandon_01) > 0
////JOIN   FilteredMaster  AS C
////ON  C.BillNumber = B.MasterBillNo
////AND C.ContNumber  = B.ContNumber
////ORDER BY A.tkid
////OFFSET (@page - 1) * @pageSize ROWS
////FETCH  NEXT @pageSize  ROWS ONLY;
