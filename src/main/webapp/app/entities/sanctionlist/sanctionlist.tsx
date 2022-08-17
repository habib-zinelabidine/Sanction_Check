import React, { useState } from 'react';

import axios from 'axios';

import Accordion from 'react-bootstrap/Accordion';
import ReactPaginate from 'react-paginate';

export const Sanctionlist = () => {
  const [list, setlist] = useState([]);
  const [Spinner, setSpinner] = useState(false);
  const [dataoffset, setoffset] = useState(null);
  const [Name, setName] = useState('');
  const [Error, setError] = useState(null);
  const [btnState, setbtnState] = useState(true);
  const [btnAccept, setbtnAccept] = useState([]);
  const [btnReject, setbtnReject] = useState([]);

  const handleSearch = data => {
    if (data.nativeEvent) {
      setlist([]);
      data.preventDefault();
    }
    setSpinner(true);
    setError(null);
    const currentPage = data.selected;
    const instance = axios.create({});
    instance({
      method: 'POST',
      url: 'https://site-api.dilisense.com/website/search',
      data: {
        query: Name,
        offset: currentPage * 15,
      },
    })
      .then(response => {
        setlist(response.data.foundRecords);
        setSpinner(false);
        setoffset(response.data.totalHits);
      })
      .catch(error => {
        setError(error.message);
        setSpinner(false);
        setlist([]);
      });
  };

  const handleAccept = (sanction, key) => () => {
    sanction.key = key;
    setbtnAccept(prev => [...prev, key]);
    const acceptedList = localStorage.getItem('acceptedList') ? JSON.parse(localStorage.getItem('acceptedList')) : [];
    if (!acceptedList.some(item => JSON.stringify(item) === JSON.stringify(sanction))) acceptedList.push(sanction);
    localStorage.setItem('acceptedList', JSON.stringify(acceptedList));
  };

  const handleReject = (sanction, key) => () => {
    setbtnReject(prev => [...prev, key]);
    const rejectedList = localStorage.getItem('rejectedList') ? JSON.parse(localStorage.getItem('rejectedList')) : [];
    if (!rejectedList.some(item => JSON.stringify(item) === JSON.stringify(sanction))) rejectedList.push(sanction);
    localStorage.setItem('rejectedList', JSON.stringify(rejectedList));
  };
  return (
    <div>
      <div className="d-flex align-items-center flex-column m-4">
        <h2>Free sanction checks and PEP screenings</h2>
        <p className=".text-secondary">
          Perform your sanction checks and PEP screenings by accessing a myriad of official government sources from around the globe.
        </p>
      </div>

      <form onSubmit={handleSearch} className="container">
        <div className="row justify-content-center">
          <div className="col-12 col-md-10 col-lg-8">
            <div className="card-body row no-gutters align-items-center">
              <div className="col-auto">
                <i className="fas fa-search h4 text-body"></i>
              </div>
              <div className="col">
                <input
                  className="form-control form-control-lg form-control-borderless"
                  id="sanctionlist-firstName"
                  name="firstName"
                  data-cy="firstName"
                  type="text"
                  placeholder="Search for a person or company..."
                  onChange={e => {
                    if (!Name) setbtnState(false);
                    setName(e.target.value);
                  }}
                  value={Name}
                />
              </div>
              <div className="col-auto">
                <button className="btn btn-lg btn-primary" type="submit" disabled={btnState}>
                  Search
                </button>
              </div>
            </div>
          </div>
        </div>
      </form>
      <div className="justify-content-center">
        {Error && (
          <div className="alert alert-danger" role="alert">
            {Error}
          </div>
        )}
        {Spinner ? (
          <div className="d-flex justify-content-center">
            <div className="spinner-border" role="status">
              <span className="sr-only">Loading...</span>
            </div>
          </div>
        ) : (
          list.length > 0 && (
            <div className="container ">
              <div className="row">
                <p>{`${dataoffset} records found`}</p>
                {list.map((result, key) => {
                  return (
                    <div className="col-6" key={key}>
                      <div className="list-group-item p-0 shadow bg-white rounded m-2">
                        <Accordion>
                          <Accordion.Header>
                            <div className="list-group-item w-100 border-0 p-0 justify-content-center">
                              <h4>{result.name}</h4>
                              <p className="text-secondary">{result.source_id}</p>
                            </div>
                          </Accordion.Header>
                          <Accordion.Body>
                            <div className="container">
                              <table className="table">
                                <tbody>
                                  {result.address && (
                                    <tr>
                                      <th scope="row ">address</th>
                                      <td>
                                        {result.address.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                      </td>
                                    </tr>
                                  )}
                                  {result.alias_names && (
                                    <tr>
                                      <th scope="row">alias_names</th>
                                      <td>
                                        {result.alias_names.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                      </td>
                                    </tr>
                                  )}
                                  {result.alias_given_names && (
                                    <tr>
                                      <th scope="row">alias_given_names</th>
                                      <td>
                                        {result.alias_given_names.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                      </td>
                                    </tr>
                                  )}
                                  {result.citizenship && (
                                    <tr>
                                      <th scope="row">citizenship</th>
                                      <td>
                                        {result.citizenship.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                      </td>
                                    </tr>
                                  )}
                                  {result.citizenship_remarks && (
                                    <tr>
                                      <th scope="row">citizenship_remarks</th>
                                      <td>
                                        {result.citizenship_remarks.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                      </td>
                                    </tr>
                                  )}
                                  {result.date_of_birth && (
                                    <tr>
                                      <th scope="row">date_of_birth</th>
                                      <td>
                                        {result.date_of_birth.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                      </td>
                                    </tr>
                                  )}
                                  {result.entity_type && (
                                    <tr>
                                      <th scope="row">entity_type</th>
                                      <td>{result.entity_type}</td>
                                    </tr>
                                  )}
                                  {result.gender && (
                                    <tr>
                                      <th scope="row">gender</th>
                                      <td>{result.gender}</td>
                                    </tr>
                                  )}
                                  {result.given_names && (
                                    <tr>
                                      <th scope="row">given_names</th>
                                      <td>
                                        {result.given_names.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                      </td>
                                    </tr>
                                  )}
                                  {result.last_names && (
                                    <tr>
                                      <th scope="row">last_names</th>
                                      <td>
                                        {result.last_names.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                      </td>
                                    </tr>
                                  )}
                                  {result.list_date && (
                                    <tr>
                                      <th scope="row">list_date</th>
                                      <td>{result.list_date}</td>
                                    </tr>
                                  )}
                                  {result.name && (
                                    <tr>
                                      <th scope="row">name</th>
                                      <td>{result.name}</td>
                                    </tr>
                                  )}
                                  {result.place_of_birth && (
                                    <tr>
                                      <th scope="row">place_of_birth</th>
                                      <td>
                                        {result.place_of_birth.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                      </td>
                                    </tr>
                                  )}
                                  {result.sanction_details && (
                                    <tr>
                                      <th scope="row">sanction_details</th>
                                      <td>
                                        {result.sanction_details.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                      </td>
                                    </tr>
                                  )}
                                  {result.source_id && (
                                    <tr>
                                      <th scope="row">source_id</th>
                                      <td>{result.source_id}</td>
                                    </tr>
                                  )}
                                  {result.source_type && (
                                    <tr>
                                      <th scope="row">source_type</th>
                                      <td>{result.source_type}</td>
                                    </tr>
                                  )}
                                </tbody>
                              </table>
                              <div className="d-flex justify-content-end">
                                <button
                                  className="btn btn-danger m-1"
                                  onClick={handleReject(result, key)}
                                  disabled={btnReject.includes(key) || btnAccept.includes(key)}
                                >
                                  {btnReject.includes(key) ? 'Rejected ✔' : 'Reject'}
                                </button>
                                <button
                                  disabled={btnReject.includes(key) || btnAccept.includes(key)}
                                  className="btn btn-success m-1"
                                  onClick={handleAccept(result, key)}
                                >
                                  {btnAccept.includes(key) ? 'Accepted ✔' : 'Accept'}
                                </button>
                              </div>
                            </div>
                          </Accordion.Body>
                        </Accordion>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          )
        )}

        {list.length > 0 && (
          <div className="m-5">
            <ReactPaginate
              previousLabel={'previous'}
              pageCount={Math.ceil(dataoffset / 15)}
              nextLabel={'next'}
              onPageChange={handleSearch}
              containerClassName={'pagination justify-content-center'}
              pageClassName={'page-item'}
              pageLinkClassName={'page-link'}
              previousClassName={'page-item'}
              previousLinkClassName={'page-link'}
              nextClassName={'page-item'}
              nextLinkClassName={'page-link'}
              activeClassName={'active'}
            />
          </div>
        )}
      </div>
    </div>
  );
};

export default Sanctionlist;
