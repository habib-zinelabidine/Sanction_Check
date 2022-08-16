import React, { useState, useEffect } from 'react';

import Accordion from 'react-bootstrap/Accordion';

const Pendinglist = () => {
  const [acceptedList, setAcceptedList] = useState([]);

  useEffect(() => {
    const accepted = localStorage.getItem('acceptedList') ? JSON.parse(localStorage.getItem('acceptedList')) : [];
    setAcceptedList(accepted);
  }, []);

  return (
    <div>
      <div className="d-flex align-items-center flex-column m-4 ">
        <h2>Accepted People</h2>
      </div>
      <div className="container ">
        <div className="row">
          {acceptedList.map((result, key) => {
            return (
              <div className="col-6" key={key}>
                <p>{result.totalHits}</p>
                <div className="list-group-item p-0 shadow bg-white rounded">
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
                        <div className="d-flex justify-content-end"></div>
                      </div>
                    </Accordion.Body>
                  </Accordion>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default Pendinglist;
