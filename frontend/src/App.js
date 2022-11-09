
function App() {
  const getResource = async (url) => {
    const result = await fetch(url, {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${localStorage.getItem("sn-token")}`,
        "Content-Type": "application/json"
      },
      redirect: "follow"
    })
    console.log("result:")
    console.log(result.json())
  }

  return (
    <div className="App">
      <p>social-network:)</p>
      <button
        onClick={() => getResource(`${process.env.REACT_APP_BASE_URL}/open`)}
      >Open</button>
      <button
        onClick={() => getResource(`${process.env.REACT_APP_BASE_URL}/user`)}
      >Closed</button>
    </div>
  );
}

export default App;
