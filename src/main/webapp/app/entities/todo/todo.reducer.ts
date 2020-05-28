import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction,
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITodo, defaultValue } from 'app/shared/model/todo.model';

export const ACTION_TYPES = {
  FETCH_TODO_LIST: 'todo/FETCH_TODO_LIST',
  FETCH_TODO: 'todo/FETCH_TODO',
  RESET: 'todo/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITodo>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type TodoState = Readonly<typeof initialState>;

// Reducer

export default (state: TodoState = initialState, action): TodoState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TODO_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TODO):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_TODO_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TODO):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TODO_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_TODO):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/todos';

// Actions

export const getEntities: ICrudGetAllAction<ITodo> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TODO_LIST,
    payload: axios.get<ITodo>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ITodo> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TODO,
    payload: axios.get<ITodo>(requestUrl),
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
