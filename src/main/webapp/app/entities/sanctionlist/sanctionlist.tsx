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

export const Sanctionlist = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [sorting, setSorting] = useState(false);

  const [list, setlist] = useState([]);

  const sanctionlistList = useAppSelector(state => state.sanctionlist.entities);
  const loading = useAppSelector(state => state.sanctionlist.loading);
  const totalItems = useAppSelector(state => state.sanctionlist.totalItems);
  const links = useAppSelector(state => state.sanctionlist.links);
  const entity = useAppSelector(state => state.sanctionlist.entity);
  const updateSuccess = useAppSelector(state => state.sanctionlist.updateSuccess);

  const [Name, setName] = useState('');

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

  const handleSearch = () => {
    const instance = axios.create({});
    instance({
      method: 'POST',
      url: 'https://site-api.dilisense.com/website/search',
      data: {
        query: Name,
        offset: 0,
      },
    })
      .then(response => {
        console.warn(response.data);
        setlist(response.data.foundRecords);
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
      <div>
        {list.length > 0 && (
          <div className="card">
            {list.map(result => {
              return (
                <div className="d-flex justify-content-center" key={result.alias_names}>
                  <button
                    className="list-group-item w-50 m-2"
                    data-bs-toggle="collapse"
                    data-bs-target="#collapseExample"
                    aria-expanded="false"
                    aria-controls="collapseExample"
                  >
                    <h4>{result.name}</h4>
                    <p className="text-secondary">{result.source_id}</p>
                  </button>

                  <div className="collapse" id="collapseExample">
                    <ul className="card-body">
                      <li>{result.citizenship}</li>
                      <li>{result.citizenship}</li>
                      <li>{result.citizenship}</li>
                      <li>{result.citizenship}</li>
                      <li>{result.citizenship}</li>
                    </ul>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
};

export default Sanctionlist;
