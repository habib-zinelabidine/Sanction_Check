import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISanctionlist } from 'app/shared/model/sanctionlist.model';
import { getEntities, reset } from './sanctionlist.reducer';

import axios from 'axios';
import { ToggleButton } from 'react-bootstrap';
import { isObject } from 'lodash';
import Accordion from 'react-bootstrap/Accordion';

export const Sanctionlist = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [sorting, setSorting] = useState(false);

  const [list, setlist] = useState([]);
  const [Spinner, setSpinner] = useState(false);
  const [dataoffset, setoffset] = useState(null);
  const [count, setcount] = useState(0);
  const [pages, setpages] = useState([]);

  const sanctionlistList = useAppSelector(state => state.sanctionlist.entities);
  const loading = useAppSelector(state => state.sanctionlist.loading);
  const totalItems = useAppSelector(state => state.sanctionlist.totalItems);
  const links = useAppSelector(state => state.sanctionlist.links);
  const entity = useAppSelector(state => state.sanctionlist.entity);
  const updateSuccess = useAppSelector(state => state.sanctionlist.updateSuccess);

  const [Name, setName] = useState('');
  const [IsOpen, setIsOpen] = useState(false);
  const [pagination, setpagination] = useState();
  const [buttonNumbers, setbuttonNumber] = useState();

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const { match } = props;

  const handleSearch = e => {
    e.preventDefault();
    setSpinner(true);
    console.warn(Spinner);

    const instance = axios.create({});
    instance({
      method: 'POST',
      url: 'https://site-api.dilisense.com/website/search',
      data: {
        query: Name,
        offset: count,
      },
    })
      .then(response => {
        console.warn(response.data);
        setlist(Object.entries(response.data.foundRecords));
        setlist(response.data.foundRecords);
        setSpinner(false);
        setoffset(response.data.totalHits);
        setcount(() => count + 15);
        console.warn(Spinner);
      })
      .catch(error => console.warn(error));
  };

  return (
    <div>
      <div className="d-flex align-items-center flex-column">
        <h2 id="sanctionlist-heading" data-cy="SanctionlistHeading">
          <Translate contentKey="jhipsterReactApp.sanctionlist.home.title"></Translate>
        </h2>
        <p className=".text-secondary">
          Perform your sanction checks and PEP screenings by accessing a myriad of official government sources from around the globe.
        </p>
      </div>
      <br></br>

      <div className="container">
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
                  onChange={e => setName(e.target.value)}
                  value={Name}
                />
              </div>
              <div className="col-auto">
                <button className="btn btn-lg btn-primary" onClick={handleSearch}>
                  Search
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="justify-content-center">
        {list.length > 0 &&
          (Spinner ? (
            <div className="d-flex justify-content-center">
              <div className="spinner-border" role="status">
                <span className="sr-only">Loading...</span>
              </div>
            </div>
          ) : (
            <div className="container ">
              <div className="row">
                {list.map((result, key) => {
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
                                  <tr>
                                    <th scope="row ">address</th>
                                    <td>
                                      {result.address &&
                                        result.address.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                    </td>
                                  </tr>
                                  <tr>
                                    <th scope="row">alias_names</th>
                                    <td>
                                      {result.alias_names &&
                                        result.alias_names.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                    </td>
                                  </tr>
                                  <tr>
                                    <th scope="row">alias_given_names</th>
                                    <td>
                                      {result.alias_given_names &&
                                        result.alias_given_names.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                    </td>
                                  </tr>
                                  <tr>
                                    <th scope="row">citizenship</th>
                                    <td>
                                      {result.citizenship &&
                                        result.citizenship.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                    </td>
                                  </tr>
                                  <tr>
                                    <th scope="row">citizenship_remarks</th>
                                    <td>
                                      {result.citizenship_remarks &&
                                        result.citizenship_remarks.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                    </td>
                                  </tr>
                                  <tr>
                                    <th scope="row">date_of_birth</th>
                                    <td>
                                      {result.date_of_birth &&
                                        result.date_of_birth.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                    </td>
                                  </tr>
                                  <tr>
                                    <th scope="row">entity_type</th>
                                    <td>{result.entity_type}</td>
                                  </tr>
                                  <tr>
                                    <th scope="row">gender</th>
                                    <td>{result.gender}</td>
                                  </tr>
                                  <tr>
                                    <th scope="row">given_names</th>
                                    <td>
                                      {result.given_names &&
                                        result.given_names.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                    </td>
                                  </tr>
                                  <tr>
                                    <th scope="row">last_names</th>
                                    <td>
                                      {result.last_names &&
                                        result.last_names.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                    </td>
                                  </tr>
                                  <tr>
                                    <th scope="row">list_date</th>
                                    <td>{result.list_date}</td>
                                  </tr>
                                  <tr>
                                    <th scope="row">name</th>
                                    <td>{result.name}</td>
                                  </tr>
                                  <tr>
                                    <th scope="row">place_of_birth</th>
                                    <td>
                                      {result.place_of_birth &&
                                        result.place_of_birth.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                    </td>
                                  </tr>
                                  <tr>
                                    <th scope="row">sanction_details</th>
                                    <td>
                                      {result.sanction_details &&
                                        result.sanction_details.map((item, i) => {
                                          return <li key={i}>{item}</li>;
                                        })}
                                    </td>
                                  </tr>
                                  <tr>
                                    <th scope="row">source_id</th>
                                    <td>{result.source_id}</td>
                                  </tr>
                                  <tr>
                                    <th scope="row">source_type</th>
                                    <td>{result.source_type}</td>
                                  </tr>
                                </tbody>
                              </table>
                              <div className="d-flex justify-content-end">
                                <button className="btn btn-danger m-1">Reject</button>
                                <button className="btn btn-success m-1">Confirm</button>
                              </div>
                            </div>
                          </Accordion.Body>
                        </Accordion>
                      </div>
                    </div>
                  );
                })}
              </div>
              <div>
                <button onClick={handleSearch}>Previous</button>
                <button onClick={handleSearch}>Next</button>
              </div>
            </div>
          ))}
      </div>
    </div>
  );
};

export default Sanctionlist;
