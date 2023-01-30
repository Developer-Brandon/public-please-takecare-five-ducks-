<%@ page import="java.net.URLDecoder" %>
<html>
<head>
    <%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8" %>
    <%@ page session="false" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ include file="../page_header.jsp" %>
    <%-- TODO: 추후 게시판 검색창에 쿠키개념 도입 예정 --%>
    <%--        request.setCharacterEncoding("UTF-8");--%>

    <%--        Cookie[] cookies = request.getCookies();--%>

    <%--        String oldSearchTitle = "";--%>

    <%--        // 만약 쿠키가 비어있지 않다면?--%>
    <%--        if(cookies != null){--%>

    <%--            for(Cookie cookie:cookies){--%>

    <%--                if(cookie.getName().equals("title")){--%>

    <%--                    oldSearchTitle = cookie.getValue();--%>

    <%--                    oldSearchTitle = URLDecoder.decode(oldSearchTitle, "UTF-8");--%>
    <%--                }--%>
    <%--            }--%>
    <%--        }--%>

    <%--        System.out.println(">>클라이언트가 전송한 쿠키정보:"+oldSearchTitle+"["+request.getRemoteAddr()+"]");--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/movie/main.css">
    <script type="text/javascript">
     function goMovieRegisterPage() {
      location.href = './register'
     }

     function goMovieModifierPage(movieNo) {
      location.href = './modifier/' + movieNo
     }

     function goMovieDetailPage(movieNo, link) {

      // todo: 추후 로그인의 개념이 잡히면 userNo를 교체할 예정입니다 .

      let data = {userNo: 100, movieNo: movieNo};

      $.ajax({
       url: "./info/view",
       data: JSON.stringify(data),
       method: "POST",
       contentType: "application/json",
       dataType: "json",
       success: function (data) {
        window.open(link)
       },
       error: function (error) {
        console.log("failed! ", error.toString())
       }
      })
     }

     function enterSearchInputValue() {

      // todo: 아무래도 JSP다 보니까, 페이지가 갱신되면서 input에 있던 검색어가 날아갑니다.
      // 추후 쿠키개념을 도입하여 보완할 예정입니다.
      location.href = './main?currentPage=1&title=' + $('.search-input').val()
     }

     $(function () {

      // search input 옆에 제목 텍스트와 아이콘을 누르면 발생하는 팝업창
      $('.search-text').click(function () {
       console.log("제목 이외에도 다른 것으로 검색할 수 있게 기능추가 예정입니다")
      })
     })
    </script>
</head>
<body>
<!-- 메뉴 시작 -->
<%@ include file="../menu.jsp" %>
<!-- 메뉴 끝 -->

<div class="body__inner">
    <%-- 상단의 제목 시작 --%>
    <div class="top">
        <div class="top__left">
            <p class="title">영화 모음</p>
        </div>
        <div class="top__right">
            <p class="register-text" onclick="goMovieRegisterPage()">등록하기</p>
        </div>
    </div>

    <%-- list section 시작 --%>
    <div class="list-section">
        <ul>
            <c:forEach var="movieVO" items="${movieListResultVO.movieVOList}">
                <li class="item" onclick="goMovieDetailPage('${movieVO.movieNo}', '${movieVO.link}')">
                    <a class="movie-no"><c:out value="${movieVO.movieNo}"/></a>
                    <a class="movie-title">
                        <c:out value="${movieVO.title}"/>
                        <span style="display:inline-block;"
                              onclick="goMovieModifierPage('${movieVO.movieNo}')">&nbsp;✍🏻
                        </span>
                    </a>
                    <a class="movie-total-number-of-episode">
                        <c:if test="movieVO.totalNumberOfEpisode != 1">
                            <span>총 ${movieVO.totalNumberOfEpisode}부작</span>
                        </c:if>
                    </a>
                    <a class="movie-director"><c:out value="${movieVO.directorName}"/></a>
                    <a class="movie-reg-dt"><c:out value="${movieVO.movieRegDt}"/></a>
                </li>
            </c:forEach>
        </ul>
    </div>

    <%-- search section 시작 --%>
    <div class="search-section">
        <div class="wrap-search-input">
            <p class="search-text">👆🏼제목</p>
            <input type="text"
                   class="search-input"
                   onkeypress="if(window.event.keyCode===13) enterSearchInputValue()"
                   value="${oldSearchTitle}"/>

        </div>
    </div>

    <%-- pagination section 시작 --%>
    <div class="pagination-section">
        <div class="pagination-section__inner">
            <c:if test="${movieListResultVO.pageHandler.totalCnt != null && movieListResultVO.pageHandler.totalCnt != 0}">
                <c:if test="${movieListResultVO.pageHandler.showPrev}">
                    <a class="first-left-side"
                       href="<c:url value="/movie/main${movieListResultVO.pageHandler.sc.getQueryString(movieListResultVO.pageHandler.beginPage)}"/>">
                        맨 앞으로
                    </a>
                </c:if>
                <ul>
                    <c:forEach var="i"
                               begin="${movieListResultVO.pageHandler.beginPage}"
                               end="${movieListResultVO.pageHandler.endPage}">
                        <li class="item ${i == movieListResultVO.pageHandler.sc.page? "active" : ""}">
                            <a href="<c:url value="/movie/main${movieListResultVO.pageHandler.sc.getQueryString(i)}"/>">
                                    ${i}
                            </a>
                        </li>
                    </c:forEach>
                </ul>
                <c:if test="${movieListResultVO.pageHandler.showNext}">
                    <a class="last-right-side"
                       href="<c:url value="/movie/main${movieListResultVO.pageHandler.sc.getQueryString(movieListResultVO.pageHandler.endPage)}"/>">
                        맨 뒤로
                    </a>
                </c:if>
            </c:if>
        </div>
    </div>
    <%-- pagination section 끝 --%>
</div>

<!-- footer 시작 -->
<%@ include file="../footer.jsp" %>
<!-- footer 끝 -->
</body>
</html>
