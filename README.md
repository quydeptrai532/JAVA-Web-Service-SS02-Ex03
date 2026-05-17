Phần 1 - Phân tích logic
1. Vai trò của HTTP Status Codes trong việc xây dựng API vững chắc (Robust API)
   HTTP Status Codes đóng vai trò là "ngôn ngữ giao tiếp chuẩn mực" giữa Client và Server.

Tách bạch tầng vận chuyển và dữ liệu: Thay vì client phải tải toàn bộ payload (body) về rồi mới parse ra xem status == "success" hay status == "error", client có thể biết ngay kết quả thông qua mã trạng thái HTTP ở header.

Tận dụng cơ chế mặc định của Client: Các thư viện gọi API trên Frontend/Mobile (như Axios, Retrofit, Fetch) đều có cơ chế tự động ném ra Exception hoặc nhảy vào block .catch() khi nhận được các mã lỗi (4xx, 5xx). Điều này giúp code ở client sạch sẽ và logic hơn rất nhiều.

2. Tại sao trả về null kèm 200 OK lại ảnh hưởng xấu đến khả năng xử lý lỗi?
   Gây hiểu lầm (False Positive): Trả về HTTP 200 OK có nghĩa là "Mọi thứ diễn ra tốt đẹp, yêu cầu của bạn đã được đáp ứng". Nếu bạn trả về 200 kèm body là null, ứng dụng client sẽ nghĩ rằng nó đã tìm thấy dữ liệu và tiến hành xử lý hiển thị.

Gây lỗi Crash App: Khi client cố gắng đọc thuộc tính (ví dụ: item.name) từ một đối tượng null hoặc chuỗi rỗng {}, ứng dụng sẽ văng lỗi NullPointerException (hoặc undefined trong JS) và crash. Nếu trả về đúng 404 Not Found, client sẽ nhảy vào luồng xử lý ngoại lệ (hiển thị thông báo "Không tìm thấy sản phẩm") một cách an toàn.

3. Tại sao Jackson Dataformat XML lại cần thiết cho Content Negotiation?
   Content Negotiation (Đàm phán nội dung) là cơ chế mà Client yêu cầu định dạng dữ liệu trả về thông qua Header Accept (ví dụ: Accept: application/json hoặc Accept: application/xml).

Spring Boot mặc định chỉ tích hợp Jackson JSON để tự động marshal (chuyển đổi) object Java sang JSON. Nó không biết cách chuyển thành XML. Bằng cách thêm dependency jackson-dataformat-xml, bạn cung cấp thêm cho Spring Boot một HttpMessageConverter chuyên xử lý XML. Nhờ đó, Spring Boot có thể "đàm phán" thành công và linh hoạt trả về đúng định dạng Client yêu cầu mà không cần thay đổi logic code ở Controller.