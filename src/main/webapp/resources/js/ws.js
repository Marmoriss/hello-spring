// ws.js
const ws = new SockJS(`http://${location.host}/spring/stomp`);
const stompClient = Stomp.over(ws);

stompClient.connect({}, (frame) => {
	console.log("connect : ", frame);
	
	// 연결 이후 구독 신청
	/*
	stompClient.subscribe("/topic/a", (message) => {
		console.log("topic/a : ", message);
	});
	
	stompClient.subscribe("/app/a", (message) => {
		console.log("app/a : ", message);
	});
	*/
	
	stompClient.subscribe("/app/notice", (message) => {
		console.log("/app/notice : ", message);
		const {body} = message;
		const {msg, time} = JSON.parse(body);
		alert(`전체 공지
================================
${msg}
================================
${new Date(time)}`);
	});
	
	stompClient.subscribe(`/app/notice/${memberId}`, (message) => {
		console.log(`/app/notice/${memberId}`, message);
		const {body} = message;
		const {msg, time} = JSON.parse(body);
		alert(`개인 공지
================================
${msg}
================================
${new Date(time)}`);
	});
	});
	
	
	
	
	








