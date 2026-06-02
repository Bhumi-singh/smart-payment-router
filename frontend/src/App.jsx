import { useEffect, useState } from "react";
import axios from "axios";

import {
  Chart as ChartJS,
  ArcElement,
  Tooltip,
  Legend
} from "chart.js";

import { Pie } from "react-chartjs-2";

ChartJS.register(
  ArcElement,
  Tooltip,
  Legend
);


function App() {

  const [stats, setStats] = useState({});
  const [payments, setPayments] = useState([]);
  const [dlq, setDlq] = useState({});
  const [cache, setCache] = useState({});

  const [searchOrderId, setSearchOrderId] = useState("");
  const [searchResult, setSearchResult] = useState(null);

  const [orderId, setOrderId] = useState("");
  const [amount, setAmount] = useState("");

  const loadData = () => {

    axios
      .get("https://smart-payment-router-1.onrender.com/dashboard")
      .then((res) => setStats(res.data));

    axios
      .get("https://smart-payment-router-1.onrender.com/dashboard/recent")
      .then((res) => setPayments(res.data));

    axios
      .get("https://smart-payment-router-1.onrender.com/dashboard/dlq-count")
      .then((res) => setDlq(res.data));

    axios
      .get("https://smart-payment-router-1.onrender.com/dashboard/cache")
      .then((res) => setCache(res.data));

  };

  useEffect(() => {

    loadData();

    const timer = setInterval(() => {
      loadData();
    }, 5000);

    return () => clearInterval(timer);

  }, []);

  const createPayment = async () => {

    try {

      await axios.post(
        "https://smart-payment-router-1.onrender.com/payments",
        {
          orderId,
          amount,
          currency: "INR"
        }
      );

      setOrderId("");
      setAmount("");

      loadData();

    } catch (error) {
      console.error(error);
    }
  };

  const chartData = {
    labels: ["Success", "Failed", "Pending"],

    datasets: [
      {
        data: [
          stats.successPayments || 0,
          stats.failedPayments || 0,
          stats.pendingPayments || 0
        ],

        backgroundColor: [
          "#198754",
          "#dc3545",
          "#ffc107"
        ]
      }
    ]
  };

  const chartOptions = {
    responsive: true,
    plugins: {
      legend: {
        position: "bottom"
      }
    }
  };

  const searchPayment = async () => {

  try {

    const response =
      await axios.get(
        `https://smart-payment-router-1.onrender.com/payments/search/${searchOrderId}`
      );

    console.log(response.data);

    if (response.data.length > 0) {
      setSearchResult(response.data[0]);
    } else {
      setSearchResult(null);
      alert("Payment not found");
    }

  } catch (error) {

    console.error(error);

    setSearchResult(null);

    alert("Payment not found");

  }

};
const updateStatus = async (id, status) => {

  try {

    await axios.put(
      `https://smart-payment-router-1.onrender.com/payments/${id}/status`,
      {
        status
      }
    );

    loadData();

    if (
      searchResult &&
      searchResult.id === id
    ) {
      searchPayment();
    }

  } catch (error) {
    console.error(error);
  }

};

  return (

    <div className="container mt-4">

      <div className="d-flex justify-content-between align-items-center mb-4">

        <h1>
          Smart Payment Router Dashboard
        </h1>

        <button
          className="btn btn-secondary"
          onClick={loadData}
        >
          Refresh
        </button>

      </div>

      {/* CREATE PAYMENT */}

      <div className="card mb-4">

        <div className="card-header">
          Create Payment
        </div>

        <div className="card-body">

          <div className="row">

            <div className="col-md-4">

              <input
                className="form-control"
                placeholder="Order ID"
                value={orderId}
                onChange={(e) =>
                  setOrderId(e.target.value)
                }
              />

            </div>

            <div className="col-md-4">

              <input
                className="form-control"
                placeholder="Amount"
                value={amount}
                onChange={(e) =>
                  setAmount(e.target.value)
                }
              />

            </div>

            <div className="col-md-4">

              <button
                className="btn btn-primary w-100"
                onClick={createPayment}
              >
                Create Payment
              </button>

            </div>

          </div>

        </div>

      </div>

      {/* STATISTICS */}

      <div className="row">

        <div className="col-md-3">

          <div className="card text-bg-primary mb-3">

            <div className="card-body">

              <h5>Total Payments</h5>

              <h2>
                {stats.totalPayments || 0}
              </h2>

            </div>

          </div>

        </div>

        <div className="col-md-3">

          <div className="card text-bg-success mb-3">

            <div className="card-body">

              <h5>Success</h5>

              <h2>
                {stats.successPayments || 0}
              </h2>

            </div>

          </div>

        </div>

        <div className="col-md-3">

          <div className="card text-bg-danger mb-3">

            <div className="card-body">

              <h5>Failed</h5>

              <h2>
                {stats.failedPayments || 0}
              </h2>

            </div>

          </div>

        </div>

        <div className="col-md-3">

          <div className="card text-bg-warning mb-3">

            <div className="card-body">

              <h5>Pending</h5>

              <h2>
                {stats.pendingPayments || 0}
              </h2>

            </div>

          </div>

        </div>

      </div>

      {/* HEALTH + PIE CHART */}

      <div className="row mb-4">

        <div className="col-md-6">

          <div className="card">

            <div className="card-header">
              System Health
            </div>

            <div className="card-body">

              <h5>
                Redis :
                <span className="badge bg-success ms-2">
                  {cache.redis || "UP"}
                </span>
              </h5>

              <h5 className="mt-3">
                DLQ Messages :
                <span className="badge bg-danger ms-2">
                  {dlq.count || 0}
                </span>
              </h5>

            </div>

          </div>

        </div>

        <div className="col-md-6">

          <div className="card">

            <div className="card-header">
              Payment Analytics
            </div>

            <div className="card-body">

              <Pie
                data={chartData}
                options={chartOptions}
              />

            </div>

          </div>

        </div>

      </div>

      <div className="card mb-4">

  <div className="card-header">
    Search Payment
  </div>

  <div className="card-body">

    <div className="row">

      <div className="col-md-8">

        <input
          className="form-control"
          placeholder="Order ID"
          value={searchOrderId}
          onChange={(e) =>
            setSearchOrderId(e.target.value)
          }
        />

      </div>

      <div className="col-md-4">

        <button
          className="btn btn-dark w-100"
          onClick={searchPayment}
        >
          Search
        </button>

      </div>

    </div>

    {searchResult && (

      <div className="mt-3">

        <h5>
          Order: {searchResult.orderId}
        </h5>

        <p>
          Status: {searchResult.status}
        </p>

        <p>
          Amount: ₹{searchResult.amount}
        </p>

      </div>

    )}

  </div>

</div>

      {/* RECENT PAYMENTS */}

      <h3 className="mb-3">
        Recent Payments
      </h3>

      <table className="table table-bordered table-striped">

        

          <thead>
            <tr>
              <th>ID</th>
              <th>Order ID</th>
              <th>Status</th>
              <th>Amount</th>
              <th>Action</th>
            </tr>
          </thead>

        

        <tbody>

          {payments.map((payment) => (

            <tr key={payment.id}>

              <td>{payment.id}</td>

              <td>{payment.orderId}</td>

              <td>

                <span
                  className={
                    payment.status === "SUCCESS"
                      ? "badge bg-success"
                      : payment.status === "FAILED"
                      ? "badge bg-danger"
                      : "badge bg-warning"
                  }
                >
                  {payment.status}
                </span>

              </td>

              <td>
  ₹ {payment.amount}
</td>

<td>

  <select
    className="form-select"
    defaultValue=""
    onChange={(e) =>
      updateStatus(
        payment.id,
        e.target.value
      )
    }
  >

    <option value="">
      Update
    </option>

    <option value="SUCCESS">
      SUCCESS
    </option>

    <option value="FAILED">
      FAILED
    </option>

    <option value="PENDING">
      PENDING
    </option>

  </select>

</td>

            </tr>

          ))}

        </tbody>

      </table>

    </div>
  );
}

export default App;