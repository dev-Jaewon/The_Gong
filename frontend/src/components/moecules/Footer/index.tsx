import styled from 'styled-components';

type MemberType = {
  src: string;
  image: string;
  name: string;
  type: string;
};

const MemberList: MemberType[] = [
  {
    src: 'https://github.com/dev-Jaewon',
    image:
      'https://user-images.githubusercontent.com/76944115/240458593-d6161514-7d84-4ba9-a097-0820efe70dc5.png',
    name: '장재원',
    type: 'Front-End',
  },
  {
    src: 'https://github.com/tari45800',
    image:
      'https://user-images.githubusercontent.com/76944115/240531338-5aa00cab-0e27-4b8c-b3d9-a9a412734499.png',
    name: '이제윤',
    type: 'Front-End',
  },
  {
    src: 'https://github.com/Pandaryu91',
    image:
      'https://user-images.githubusercontent.com/76944115/240531585-ea9c7fa0-e57c-4093-bb5e-4e013f66b26a.png',
    name: '류성현',
    type: 'Front-End',
  },
  {
    src: 'https://github.com/ChaeEunLee00',
    image:
      'https://user-images.githubusercontent.com/76944115/240489012-4fdcbef4-e288-4dc1-b803-17cd2603d26d.png',
    name: '이채은',
    type: 'Back-End',
  },
  {
    src: 'https://github.com/SOONUK-k',
    image:
      'https://user-images.githubusercontent.com/76944115/240488271-4c45cf54-43b8-41f6-933b-bbe134841591.png',
    name: '권순욱',
    type: 'Back-End',
  },
  {
    src: 'https://github.com/gtaegyeong',
    image:
      'https://user-images.githubusercontent.com/76944115/240488049-d200fa49-1729-4bed-a088-6db091f331ba.png',
    name: '강태경',
    type: 'Back-End',
  },
];

export const Footer = () => {
  return (
    <FooterContainer>
      <TeamNameContainer>
        <TeamName>맑은 눈의 광인들</TeamName>
      </TeamNameContainer>
      <MemberListContainer>
        {MemberList.map((member, index) => (
          <MemberListItem key={index}>
            <MemberLink href={member.src}>
              <MemberImage src={member.image} alt={member.name} />
              <MemberName>{member.name}</MemberName>
              <MemberTypeText>{member.type}</MemberTypeText>
            </MemberLink>
          </MemberListItem>
        ))}
      </MemberListContainer>
    </FooterContainer>
  );
};

export default Footer;

const FooterContainer = styled.footer`
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: white;
  margin-top: 10rem;
`;

const TeamNameContainer = styled.div`
  margin-top: 20px;
  text-align: center;
`;

const TeamName = styled.span`
  font-size: 24px;
  font-weight: bold;
`;

const MemberListContainer = styled.ul`
  display: flex;
  list-style: none;
  padding: 0;
`;

const MemberListItem = styled.li`
  margin-right: 20px;
  &:last-child {
    margin-right: 0;
  }
`;

const MemberLink = styled.a`
  display: flex;
  flex-direction: column;
  align-items: center;
  text-decoration: none;
`;

const MemberImage = styled.img`
  margin-top: 15px;
  width: 100%;
  height: 70px;
`;

const MemberName = styled.span`
  margin-top: 10px;
  font-size: 18px;
`;

const MemberTypeText = styled.span`
  margin-top: 5px;
  font-size: 14px;
  color: #888888;
`;