export const searchData = {
  data: [
    {
      room_id: 1,
      title: '스터디방 제목',
      info: '스터디방 정보',
      admin: {
        member_id: 1,
        nickname: '방장 닉네임',
        about_me: '방장에 대한 간단한 소개',
        image_irl: 'AWS S3에 저장되어있는 프로필 이미지 주소',
      },
      image_url:
        'https://cdn.eyesmag.com/content/uploads/posts/2020/08/11/the-patrick-star-show-spongebob-squarepants-spin-off-1-516d0d4f-fcf0-4106-ab95-a407167fee2c.jpg',
      member_max_count: 6,
      member_current_count: 3,
      is_private: true,
      created_at: '2023-04-17T17:19:50.26007',
      last_modified_at: '2023-04-17T17:19:50.26007',
      tags: [
        '공무원 시험 준비',
        '모각코',
        '모각코',
        '모각코',
        '공무원 시험 준비',
        '공무원 시험 준비',
        '공무원 시험 준비',
      ],
      favorite_count: 10,
    },
  ],
  pageInfo: {
    page: 1,
    size: 10,
    total_elements: 100,
    total_pages: 10,
  },
};
