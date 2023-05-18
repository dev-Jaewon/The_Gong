export const formatQueryString = (
  query: string,
  key: string,
  value: string
) => {
  const sortObj = query
    .slice(1)
    .split('&')
    .reduce((obj, query) => {
      const [key, value] = query.split('=');
      obj[key] = value;
      return obj;
    }, {} as { [key: string]: string });

  sortObj[key] = value;

  return Object.keys(sortObj).reduce((str, key, index) => {
    str += index === 0 ? '?' : '&';
    return (str += `${key}=${sortObj[key]}`);
  }, '');
};
