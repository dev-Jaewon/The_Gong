import styled from "styled-components";

const RoomInfoContainer = styled.div`
  font-size: 13px;
  color: #4a5056;
  font-weight: 500;
  margin-bottom: 10px;
`

interface RoomInfoProps {
  roomInfo: string;
}

const RoomInfo = ({ roomInfo }: RoomInfoProps) => {
  return (
    <RoomInfoContainer>
      {roomInfo}
    </RoomInfoContainer>
  )
}

export default RoomInfo;