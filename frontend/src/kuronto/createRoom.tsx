import SockJS from 'sockjs-client';

function CreateRoom() {
  const ws = new SockJS('https://7da3-211-193-143-25.ngrok-free.app/groupcall');

  ws.onopen = () => {
    console.log('연결됨')
  }

  type Message = {
    id: string;
    name: string;
    room: string;
  }
  
  const sendJoin = () => {
    var message: Message = {
      id: 'joinRoom',
      name: "이제윤의 name",
      room: "이제윤의 room",
    }
    sendMessage(message);
  }
  
  function sendMessage(message: Message) {
    var jsonMessage = JSON.stringify(message);
    console.log("이걸 보냈어 : " + jsonMessage)
    ws.send(jsonMessage);
  }
  return(
    <div id="container">
      <div id="wrapper">
        <div id="join" className="animate join">
          <h1>Join a Room</h1>
          <form acceptCharset="UTF-8">
            <p>
              <input
                type="text"
                name="name"
                value=""
                id="name"
                placeholder="Username"
                required
              />
            </p>
            <p>
              <input
                type="text"
                name="room"
                value=""
                id="roomName"
                placeholder="Room"
                required
              />
            </p>
            <p className="submit">
              <input type="button" name="commit" value="Join!" onClick={()=>{sendJoin()}}/>
            </p>
          </form>
        </div>
        <div id="room" style={{ display: "none" }}>
          <h2 id="room-header"></h2>
          <div id="participants"></div>
          <input type="button" id="button-leave" value="Leave room" />
        </div>
      </div>
    </div>

  )
  
}
export default CreateRoom;
