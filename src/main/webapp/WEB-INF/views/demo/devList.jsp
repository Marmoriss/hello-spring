<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Dev 목록" name="title"/>
</jsp:include>
<table class="table w-75 mx-auto" style="text-align:center;">
	<thead>
		<tr>
		  <th scope="col">번호</th>
		  <th scope="col">이름</th>
		  <th scope="col">경력</th>
		  <th scope="col">이메일</th>
		  <th scope="col">성별</th>
		  <th scope="col">개발가능언어</th>
		  <th scope="col">등록일시</th>
		  <th scope="col">수정 | 삭제</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${list}" var="dev">
			<tr>
				<td>${dev.no}</td>
				<td>${dev.name}</td>
				<td>${dev.career}</td>
				<td>${dev.email}</td>
				<td>${dev.gender}</td>
				<td>
					<c:forEach items="${dev.lang}" var="lang" varStatus="vs">
						${lang}${not vs.last ? "," : ""}
					</c:forEach>
				</td>
				<td>
					<fmt:parseDate value="${dev.createdAt}" pattern="yyyy-MM-dd'T'HH:mm" var="createdAt"/>
					<fmt:formatDate value="${createdAt}" pattern="yyyy/MM/dd HH:mms"/>
				</td>
			    <td>
			        <!--https://getbootstrap.com/docs/4.0/components/buttons/#outline-buttons-->
			        <button type="button" class="btn btn-outline-secondary" onclick="updateDev(${dev.no});">수정</button>
			        <button type="button" class="btn btn-outline-danger" onclick="deleteDev(${dev.no});">삭제</button>
			    </td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<form name="deleteFrm" method="POST">
	<input type="hidden" name="no" value=""/>
</form>

<script>
/**
 * GET /demo/updateDev.do?no=123
 * POST /demo./updateDev.do
 */
 
const updateDev = (no) => {
	 location.href = `${pageContext.request.contextPath}/demo/updateDev.do?no=\${no}`;
}

/**
 * POST /demo/deleteDev.do
 */
 
 const deleteDev = (no) => {
	 if(!confirm("정말 삭제하시겠습니까?")) return;
	 
	 const frm = document.deleteFrm;
	 frm.action = `${pageContext.request.contextPath}/demo/deleteDev.do`;
	 frm.no.value = no;
	 frm.submit();
 }
 
</script>
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>

















