
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
        onClick={() => getResource("http://localhost:8080/open")}
      >Open</button>
      <button
        onClick={() => getResource("http://localhost:8080/user")}
      >Closed</button>
    </div>
  );
}

export default App;
